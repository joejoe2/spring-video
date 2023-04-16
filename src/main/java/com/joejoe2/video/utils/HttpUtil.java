package com.joejoe2.video.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class HttpUtil {
  public static String extractAccessToken(HttpServletRequest request) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader != null) {
      return authHeader.replace("Bearer ", "");
    } else {
      return request.getParameter("access_token");
    }
  }

  public static Map<String, String> splitQuery(String query) {
    Map<String, String> query_pairs = new HashMap<>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
      int idx = pair.indexOf("=");
      if (idx != -1)
        query_pairs.put(
            URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8),
            URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
    }
    return query_pairs;
  }
}
