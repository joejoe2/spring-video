package com.joejoe2.video.utils;

import com.joejoe2.video.data.UserDetail;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
  }

  public static UserDetail currentUserDetail() throws AuthenticationException {
    if (!isAuthenticated())
      throw new InternalAuthenticationServiceException("has not been authenticated !");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (UserDetail) authentication.getPrincipal();
  }

  public static void setCurrentUserDetail(UserDetail userDetail) {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
