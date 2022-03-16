package com.seat.rescuesim.simserver.sim;

public class SimException extends RuntimeException {

    /** Accepts the explanation for the error. */
    public SimException(String msg) {
        super(msg);
    }

    /** Accepts the explanation and the cause of the error. */
    public SimException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
