package com.joejoe2.video.exception;

public class ControllerConstraintViolation extends Exception {
  private final int rejectStatus;
  private final String rejectMessage;

  public ControllerConstraintViolation(int rejectStatus, String rejectMessage) {
    this.rejectStatus = rejectStatus;
    this.rejectMessage = rejectMessage;
  }

  public int getRejectStatus() {
    return rejectStatus;
  }

  public String getRejectMessage() {
    return rejectMessage;
  }
}
