package com.joejoe2.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joejoe2.worker.data.EventDto;
import com.joejoe2.worker.repository.VideoEventResultRepository;
import com.joejoe2.worker.util.FileUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

@Component
public class VideoEventHandler {
  @Autowired ObjectMapper objectMapper;
  @Autowired VideoService videoService;
  @Autowired VideoEventResultRepository eventResultRepository;
  private static final Logger logger = LoggerFactory.getLogger(VideoEventHandler.class);
  @Autowired ThreadPoolTaskExecutor executor;

  @PostConstruct
  private void init() {
    executor.setCorePoolSize(1);
  }

  @Autowired KafkaManager kafkaManager;

  @KafkaListener(
      id = "video_event_consumer",
      topics = "spring-video.public.video_event",
      groupId = "spring-video.public.video_event:consumer",
      concurrency = "1")
  void consume(String message, Acknowledgment acknowledgment) {
    kafkaManager.pauseConsume("video_event_consumer");
    executor
        .submitListenable(
            () -> {
              try {
                process(message, acknowledgment);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .addCallback(
            (res) -> {
              kafkaManager.resumeConsumer("video_event_consumer");
            },
            ex -> {
              logger.info(ex.getMessage());
              try {
                Thread.sleep(15000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              kafkaManager.resumeConsumer("video_event_consumer");
              kafkaManager.restartConsumer("video_event_consumer");
            });
  }

  private void process(String message, Acknowledgment acknowledgment) {
    // read event
    EventDto eventDto;
    try {
      eventDto = objectMapper.readValue(message, EventDto.class);
    } catch (Exception e) {
      throw new RuntimeException("cannot deserialize video event: " + message);
    }
    logger.info(eventDto.getEvent().toString());

    // check processed before
    if (eventResultRepository.existsById(eventDto.getEvent().getId())) {
      acknowledgment.acknowledge();
      logger.info("duplicated event !");
      return;
    }
    // Thread.sleep(1000*60*10);

    // download
    File file;
    try {
      file = videoService.download(eventDto.getEvent().getSource(), eventDto.getEvent().getId());
      file.deleteOnExit();
    } catch (Exception e) {
      e.printStackTrace();
      videoService.handleFailure(eventDto, "cannot retrieve video file !");
      acknowledgment.acknowledge();
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
      acknowledgment.acknowledge();
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
      acknowledgment.acknowledge();
      file.delete();
      FileSystemUtils.deleteRecursively(tempDir);
      return;
    }

    // success
    videoService.handleSuccess(eventDto, streamPath);
    acknowledgment.acknowledge();
    file.delete();
    FileSystemUtils.deleteRecursively(tempDir);
  }
}
