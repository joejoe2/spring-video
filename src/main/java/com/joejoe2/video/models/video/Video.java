package com.joejoe2.video.models.video;

import com.joejoe2.video.models.User;
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
@Table(indexes = {@Index(columnList = "createAt DESC")})
public class Video {
  @Version
  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()")
  private Instant version;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(unique = true, updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  User owner;

  @Column(length = 256, nullable = false)
  private String title;

  @Column(length = 32, nullable = false)
  @Enumerated(EnumType.STRING)
  private VideoStatus status = VideoStatus.PROCESSING;

  @Column(length = 1024, nullable = false)
  private String eventResult = "";

  @Column(length = 512, nullable = false)
  private String path = "";

  @Column(nullable = false)
  private Instant createAt;

  @PrePersist
  private void prePersist() {
    createAt = Instant.now();
  }

  public Video(User owner, String title) {
    this.owner = owner;
    this.title = title;
  }
}
