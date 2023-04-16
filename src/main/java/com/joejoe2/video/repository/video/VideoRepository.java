package com.joejoe2.video.repository.video;

import com.joejoe2.video.models.video.Video;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, UUID> {
  Optional<Video> findById(UUID id);
}
