package com.joejoe2.video.models.video;

public enum ResultStatus {
  SUCCESS("SUCCESS"),
  FAIL("FAIL");

  private final String value;

  ResultStatus(String role) {
    this.value = role;
  }

  public String toString() {
    return value;
  }
}
