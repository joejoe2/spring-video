package com.joejoe2.worker.service;

import com.joejoe2.worker.config.MinioConfiguration;
import com.joejoe2.worker.data.EventDto;
import com.joejoe2.worker.model.ResultStatus;
import com.joejoe2.worker.model.VideoEvent;
import com.joejoe2.worker.model.VideoEventResult;
import com.joejoe2.worker.repository.VideoEventResultRepository;
import com.joejoe2.worker.util.VideoUtil;
import java.io.File;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoServiceImpl implements VideoService {
  @Autowired MinioService minioService;
  @Autowired MinioConfiguration minioConfiguration;
  @Autowired VideoEventResultRepository eventResultRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleSuccess(EventDto eventDto, String path) {
    VideoEvent event = eventDto.getEvent();
    VideoEventResult result = new VideoEventResult(event, path, ResultStatus.SUCCESS);
    eventResultRepository.saveAndFlush(result);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void handleFailure(EventDto eventDto, String message) {
    VideoEvent event = eventDto.getEvent();
    VideoEventResult result = new VideoEventResult(event, message, ResultStatus.FAIL);
    eventResultRepository.saveAndFlush(result);
  }

  @Override
  public void convert(File source, File workDir) throws Exception {
    VideoUtil.transcodeToM3u8(source, workDir);
  }

  @Override
  public File download(String source, String outputName) throws Exception {
    if (source != null && source.startsWith("minio.bucket.store://")) {
      return minioService.downloadObject(
          minioConfiguration.getStoreBucket(),
          source.replaceFirst("minio.bucket.store://", ""),
          outputName);
    } else {
      throw new IllegalArgumentException("unknown source %s !" + source);
    }
  }

  @Override
  public void upload(File workDir, String prefix) throws Exception {
    for (File file : Objects.requireNonNull(workDir.listFiles())) {
      minioService.uploadObject(
          file, minioConfiguration.getStreamBucket(), prefix + file.getName());
    }
  }
}
