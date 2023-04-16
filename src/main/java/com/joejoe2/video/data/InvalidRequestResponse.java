package com.joejoe2.video.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.TreeSet;
import lombok.Data;

@Data
public class InvalidRequestResponse {
  @Schema(
      description = "errors in each field of request payload",
      title = "errors in each field of request payload")
  Map<String, TreeSet<String>> errors;

  public InvalidRequestResponse(Map<String, TreeSet<String>> errors) {
    this.errors = errors;
  }
}
