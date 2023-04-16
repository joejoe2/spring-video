package com.joejoe2.video.service.video;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joejoe2.video.data.video.EventResultDto;
import com.joejoe2.video.models.video.EventResult;
import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class VideoEventHandler {
  @Autowired ObjectMapper objectMapper;

  @Autowired VideoService videoService;
  private static final Logger logger = LoggerFactory.getLogger(VideoEventHandler.class);

  @KafkaListener(
      topics = "spring-worker.public.video_event_result",
      groupId = "consumer",
      concurrency = "1")
  void consume(String message, Consumer consumer) throws JsonProcessingException {
    EventResult result = objectMapper.readValue(message, EventResultDto.class).getResult();
    logger.info(result.toString());
    try {
      videoService.onVideoEventComplete(result);
      consumer.commitSync();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(ex.getMessage());
    }
  }
}
