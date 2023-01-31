package com.seat.sim.common.json;

/** An Exception class to represent JSON decoding errors. */
public class JsonException extends RuntimeException {

    /** Accepts the explanation for the error. */
    public JsonException(String msg) {
        super(msg);
    }

    /** Accepts the explanation and the cause of the error. */
    public JsonException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
