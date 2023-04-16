package com.joejoe2.video.service.redis;

import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
  @Autowired private StringRedisTemplate redisTemplate;

  @Override
  public void set(String key, String value, Duration duration) {
    redisTemplate.opsForValue().setIfAbsent(key, value, duration);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(key));
  }

  @Override
  public boolean has(String key) {
    return redisTemplate.hasKey(key);
  }
}
