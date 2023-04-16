package com.joejoe2.worker.model;

import java.time.Instant;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VideoEventResult {
  @Id
  @Column(unique = true, updatable = false, nullable = false)
  private String id;

  @Column(length = 128, nullable = false)
  String videoId;

  @Column(length = 128, nullable = false)
  String userId;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private Operation operation;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private ResultStatus status;

  @Column(length = 1024, nullable = false)
  private String detail;

  @Column(updatable = false, nullable = false)
  private Instant startAt;

  @Column(updatable = false, nullable = false)
  private Instant completeAt;

  @PrePersist
  private void prePersist() {
    completeAt = Instant.now();
  }

  public VideoEventResult(VideoEvent event, String detail, ResultStatus status) {
    id = event.getId();
    videoId = event.getVideoId();
    userId = event.getUserId();
    operation = event.getOperation();
    this.status = status;
    this.detail = detail;
    startAt = event.getStartAt();
  }
}
