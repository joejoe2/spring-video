package com.joejoe2.video.models.video;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {@Index(columnList = "videoId"), @Index(columnList = "startAt")})
public class VideoEvent {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(unique = true, updatable = false, nullable = false)
  private UUID id;

  @Column(length = 128, nullable = false, updatable = false)
  String videoId;

  @Column(length = 128, nullable = false, updatable = false)
  String userId;

  @Column(length = 1024, nullable = false, updatable = false)
  String source = "";

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private Operation operation;

  @Column(nullable = false)
  private Instant startAt;

  @PrePersist
  private void prePersist() {
    startAt = Instant.now();
  }

  public VideoEvent(String videoId, String userId, Operation operation) {
    this.videoId = videoId;
    this.userId = userId;
    this.operation = operation;
  }
}
