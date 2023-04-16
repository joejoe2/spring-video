package com.joejoe2.video.models.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventResult {
  private String id;

  @JsonProperty("video_id")
  private String videoId;

  @JsonProperty("user_id")
  private String userId;

  private Operation operation;

  private ResultStatus status;

  private String detail;

  @JsonProperty("start_at")
  @JsonDeserialize(converter = MicroToInstantConverter.class)
  private Instant startAt;

  @JsonProperty("complete_at")
  @JsonDeserialize(converter = MicroToInstantConverter.class)
  private Instant completeAt;
}
