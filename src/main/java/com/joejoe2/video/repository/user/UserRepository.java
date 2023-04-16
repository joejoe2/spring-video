package com.joejoe2.video.repository.user;

import com.joejoe2.video.models.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  @Override
  @Cacheable(value = "storage-users")
  boolean existsById(UUID uuid);

  Optional<User> findById(UUID id);

  Optional<User> getByUserName(String username);
}
