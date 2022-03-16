package com.seat.rescuesim.client.core;

public class CoreException extends RuntimeException {

    /** Accepts the explanation for the error. */
    public CoreException(String msg) {
        super(msg);
    }

    /** Accepts the explanation and the cause of the error. */
    public CoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
