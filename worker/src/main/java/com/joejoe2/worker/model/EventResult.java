package com.joejoe2.worker.model;

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
  private String eventId;
  private String videoId;

  private String userId;

  private Operation operation;

  private ResultStatus status;

  private String detail;

  private Instant startAt;

  private Instant completeAt;
}
