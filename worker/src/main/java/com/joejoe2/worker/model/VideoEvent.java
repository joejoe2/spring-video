package com.joejoe2.worker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.joejoe2.worker.data.MicroToInstantConverter;
import java.time.Instant;
import javax.persistence.*;
import lombok.*;

@Data
public class VideoEvent {
  private String id;

  @JsonProperty("video_id")
  String videoId;

  @JsonProperty("user_id")
  String userId;

  String source;

  private Operation operation;

  @JsonDeserialize(converter = MicroToInstantConverter.class)
  @JsonProperty("start_at")
  private Instant startAt;
}
