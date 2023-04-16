package com.joejoe2.video.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class IPUtils {
  private static final String REQUEST_IP_ATTRIBUTE = "REQUEST_IP";

  public static void setRequestIP(String ip) {
    RequestContextHolder.currentRequestAttributes()
        .setAttribute(REQUEST_IP_ATTRIBUTE, ip, RequestAttributes.SCOPE_REQUEST);
  }

  public static String getRequestIP() {
    return (String)
        RequestContextHolder.currentRequestAttributes()
            .getAttribute(REQUEST_IP_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
  }
}
