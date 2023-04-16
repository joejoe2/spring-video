package com.joejoe2.worker.model;

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
