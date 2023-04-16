package com.joejoe2.video.repository.video;

import com.joejoe2.video.models.video.VideoEvent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoEventRepository extends JpaRepository<VideoEvent, UUID> {
  Optional<VideoEvent> findById(UUID id);
}
