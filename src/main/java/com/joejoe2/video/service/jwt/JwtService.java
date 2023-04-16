package com.joejoe2.video.service.jwt;

import com.joejoe2.video.data.UserDetail;
import com.joejoe2.video.exception.InvalidTokenException;

public interface JwtService {
  /**
   * retrieve UserDetail from access token
   *
   * @param token access token in plaintext
   * @return related UserDetail with the access token
   * @throws InvalidTokenException if the access token is invalid
   */
  UserDetail getUserDetailFromAccessToken(String token) throws InvalidTokenException;

  boolean isAccessTokenInBlackList(String accessPlainToken);
}
