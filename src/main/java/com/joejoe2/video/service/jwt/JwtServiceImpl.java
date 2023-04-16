package com.joejoe2.video.service.jwt;

import com.joejoe2.video.config.JwtConfig;
import com.joejoe2.video.data.UserDetail;
import com.joejoe2.video.exception.InvalidTokenException;
import com.joejoe2.video.service.redis.RedisService;
import com.joejoe2.video.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
  @Autowired JwtConfig jwtConfig;
  @Autowired RedisService redisService;

  private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

  @Override
  public UserDetail getUserDetailFromAccessToken(String token) throws InvalidTokenException {
    return JwtUtil.extractUserDetailFromAccessToken(jwtConfig.getPublicKey(), token);
  }

  @Override
  public boolean isAccessTokenInBlackList(String accessPlainToken) {
    return redisService.has("revoked_access_token:{" + accessPlainToken + "}");
  }
}
