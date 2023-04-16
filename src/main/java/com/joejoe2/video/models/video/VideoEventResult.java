package com.joejoe2.video.models.video;

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

  @Column(length = 128, nullable = false, updatable = false)
  String videoId;

  @Column(length = 128, nullable = false, updatable = false)
  String userId;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private Operation operation;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private ResultStatus status;

  @Column(length = 1024, nullable = false)
  private String detail;

  @Column(nullable = false)
  private Instant startAt;

  @Column(nullable = false)
  private Instant completeAt;

  public VideoEventResult(EventResult result) {
    id = result.getId();
    videoId = result.getVideoId();
    userId = result.getUserId();
    operation = result.getOperation();
    status = result.getStatus();
    detail = result.getDetail();
    startAt = result.getStartAt();
    completeAt = result.getCompleteAt();
  }
}
