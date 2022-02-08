package com.seat.rescuesim.common.json;

/** An Exception class to represent JSON decoding errors. */
public class JSONException extends RuntimeException {

    /** Accepts the explanation for the error. */
    public JSONException(String msg) {
        super(msg);
    }

    /** Accepts the explanation and the cause of the error. */
    public JSONException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
