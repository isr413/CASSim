package com.seat.sim.server.core;

public class ServerException extends RuntimeException {

  /** Accepts the explanation for the error. */
  public ServerException(String msg) {
    super(msg);
  }

  /** Accepts the explanation and the cause of the error. */
  public ServerException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
