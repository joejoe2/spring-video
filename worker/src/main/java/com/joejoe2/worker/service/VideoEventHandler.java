package com.joejoe2.worker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joejoe2.worker.data.EventDto;
import com.joejoe2.worker.repository.VideoEventResultRepository;
import com.joejoe2.worker.util.FileUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
public class VideoEventHandler {
  @Autowired ObjectMapper objectMapper;
  @Autowired VideoService videoService;
  @Autowired VideoEventResultRepository eventResultRepository;
  private static final Logger logger = LoggerFactory.getLogger(VideoEventHandler.class);

  @KafkaListener(
      topics = "spring-video.public.video_event",
      groupId = "consumer",
      concurrency = "1")
  void consume(String message, Consumer consumer) throws JsonProcessingException {
    EventDto eventDto = objectMapper.readValue(message, EventDto.class);
    logger.info(eventDto.getEvent().toString());
    // check processed before
    if (eventResultRepository.existsById(eventDto.getEvent().getId())) {
      consumer.commitSync();
      return;
    }
    // download
    File file;
    try {
      file = videoService.download(eventDto.getEvent().getSource(), eventDto.getEvent().getId());
      file.deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
      videoService.handleFailure(eventDto, "cannot retrieve video file !");
      consumer.commitSync();
      return;
    }
    // process
    File tempDir;
    try {
      tempDir = Files.createTempDirectory(Path.of("tmp"), "tmp-" + file.getName()).toFile();
      FileUtil.deleteDirOnExit(tempDir);
      videoService.convert(file, tempDir);
    } catch (Exception e) {
      e.printStackTrace();
      videoService.handleFailure(eventDto, "cannot convert video file !");
      consumer.commitSync();
      file.delete();
      return;
    }
    // upload converted video
    String streamPath =
        "user/"
            + eventDto.getEvent().getUserId()
            + "/video/"
            + eventDto.getEvent().getVideoId()
            + "/";
    try {
      videoService.upload(tempDir, streamPath);
    } catch (Exception e) {
      e.printStackTrace();
      videoService.handleFailure(eventDto, "cannot upload converted video file !");
      consumer.commitSync();
      file.delete();
      FileSystemUtils.deleteRecursively(tempDir);
      return;
    }
    // success
    videoService.handleSuccess(eventDto, streamPath);
    consumer.commitSync();
    file.delete();
    FileSystemUtils.deleteRecursively(tempDir);
  }
}
