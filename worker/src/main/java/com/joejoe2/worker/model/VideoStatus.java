package com.joejoe2.worker.model;

public enum VideoStatus {
  PROCESSING("PROCESSING"),
  READY("READY"),
  DELETING("DELETING"),
  ERROR("ERROR");

  private final String value;

  VideoStatus(String role) {
    this.value = role;
  }

  public String toString() {
    return value;
  }
}
