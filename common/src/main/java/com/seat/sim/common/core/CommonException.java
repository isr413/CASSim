package com.seat.sim.common.core;

/** An Exception class to represent logical errors in the Common library. */
public class CommonException extends RuntimeException {

  /** Accepts the explanation for the error. */
  public CommonException(String msg) {
    super(msg);
  }

  /** Accepts the explanation and the cause of the error. */
  public CommonException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
