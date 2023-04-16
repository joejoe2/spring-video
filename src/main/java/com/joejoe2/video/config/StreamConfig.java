package com.joejoe2.video.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class StreamConfig {
  @Value("${server.stream.prefix:http://localhost:8082/api/video/}")
  private String prefix;
}
