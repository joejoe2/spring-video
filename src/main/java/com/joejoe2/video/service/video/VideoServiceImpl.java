package com.joejoe2.video.service.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joejoe2.video.config.ObjectStorageConfiguration;
import com.joejoe2.video.config.StreamConfig;
import com.joejoe2.video.data.video.VideoProfile;
import com.joejoe2.video.exception.DoesNotExist;
import com.joejoe2.video.models.User;
import com.joejoe2.video.models.video.*;
import com.joejoe2.video.repository.VideoEventResultRepository;
import com.joejoe2.video.repository.user.UserRepository;
import com.joejoe2.video.repository.video.VideoEventRepository;
import com.joejoe2.video.repository.video.VideoRepository;
import com.joejoe2.video.service.storage.ObjectStorageService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Service
public class VideoServiceImpl implements VideoService {
  @Autowired UserRepository userRepository;
  @Autowired VideoRepository videoRepository;
  @Autowired VideoEventRepository eventRepository;
  @Autowired ObjectMapper objectMapper;
  @Autowired VideoEventResultRepository resultRepository;
  @Autowired ObjectStorageService objectStorageService;
  @Autowired ObjectStorageConfiguration objectStorageConfiguration;
  @Autowired StreamConfig streamConfig;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public VideoProfile createFromObjectStorage(UUID userId, String title, String objectName) {
    User user = userRepository.findById(userId).orElseThrow();
    Video video = new Video(user, title);
    video = videoRepository.saveAndFlush(video);
    VideoEvent event =
        new VideoEvent(video.getId().toString(), userId.toString(), Operation.PROCESS);
    event.setSource("minio.bucket.store://" + objectName);
    eventRepository.save(event);
    return new VideoProfile(video);
  }

  @Override
  @Transactional(readOnly = true)
  public StreamingResponseBody m3u8Index(UUID videoId) throws DoesNotExist {
    // User user = userRepository.findById(userId).orElseThrow();
    // todo: check accessible ?
    Video video =
        videoRepository
            .findById(videoId)
            .orElseThrow(() -> new DoesNotExist("target video does not exist !"));
    if (video.getStatus().equals(VideoStatus.PROCESSING))
      throw new DoesNotExist("target video is still under processing !");

    String tsPrefix = streamConfig.getPrefix() + videoId + "/";
    try {
      List<String> files =
          objectStorageService.listFiles(
              objectStorageConfiguration.getStreamBucket(), video.getPath());
      String target =
          files.stream().filter((f) -> f.endsWith("index.m3u8")).findFirst().orElseThrow();
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(
                  objectStorageService.readFile(
                      objectStorageConfiguration.getStreamBucket(), target)));
      return outputStream -> {
        try {
          String line;
          while ((line = reader.readLine()) != null) {
            if (line.endsWith(".ts")) line = tsPrefix + line;
            outputStream.write(line.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
          }
          outputStream.flush();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          reader.close();
          outputStream.close();
        }
      };
    } catch (Exception e) {
      e.printStackTrace();
      throw new DoesNotExist("cannot retrieve target m3u8 !");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public StreamingResponseBody ts(UUID videoId, String filename) throws DoesNotExist {
    // User user = userRepository.findById(userId).orElseThrow();
    // todo: check accessible ?
    Video video =
        videoRepository
            .findById(videoId)
            .orElseThrow(() -> new DoesNotExist("target video does not exist !"));
    if (video.getStatus().equals(VideoStatus.PROCESSING))
      throw new DoesNotExist("target video is still under processing !");

    try {
      InputStream inputStream =
          objectStorageService.readFile(
              objectStorageConfiguration.getStreamBucket(),
              "user/" + video.getOwner().getId() + "/video/" + videoId + "/" + filename);
      return outputStream -> {
        try {
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
          }
          outputStream.flush();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          inputStream.close();
          outputStream.close();
        }
      };
    } catch (Exception e) {
      e.printStackTrace();
      throw new DoesNotExist("cannot retrieve target ts !");
    }
  }

  @Override
  public List<VideoProfile> list() {
    return videoRepository.findAll().stream().map(VideoProfile::new).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public VideoProfile profile(UUID videoId) throws DoesNotExist {
    // User user = userRepository.findById(userId).orElseThrow();
    // todo: check accessible ?
    Video video =
        videoRepository
            .findById(videoId)
            .orElseThrow(() -> new DoesNotExist("target video does not exist !"));
    return new VideoProfile(video);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
  public void onVideoEventComplete(EventResult result) throws Exception {
    Video video = videoRepository.findById(UUID.fromString(result.getVideoId())).orElse(null);
    if (video == null) return;
    // check is processed before to prevent duplicated
    VideoEventResult r = resultRepository.findById(result.getId()).orElse(null);
    if (r != null) return;
    r = new VideoEventResult(result);
    // if operation fail(process or delete)
    if (ResultStatus.FAIL.equals(result.getStatus())) {
      video.setStatus(VideoStatus.ERROR);
      video.setEventResult(objectMapper.writeValueAsString(result));
      videoRepository.save(video);
      resultRepository.save(r);
      return;
    }
    // if operation success
    if (Operation.DELETE.equals(result.getOperation())
        && VideoStatus.DELETING.equals(video.getStatus())) {
      videoRepository.delete(video);
      resultRepository.save(r);
    } else if (Operation.PROCESS.equals(result.getOperation())
        && VideoStatus.PROCESSING.equals(video.getStatus())) {
      video.setStatus(VideoStatus.READY);
      video.setPath(result.getDetail());
      video.setEventResult(objectMapper.writeValueAsString(result));
      videoRepository.save(video);
      resultRepository.save(r);
    } else {
      throw new RuntimeException(
          "unexpected Video:%s, VideoEventResult:%s".formatted(video, result));
    }
  }
}
