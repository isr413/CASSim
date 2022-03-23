package com.seat.rescuesim.client.util;

public class ClientException extends RuntimeException {

    /** Accepts the explanation for the error. */
    public ClientException(String msg) {
        super(msg);
    }

    /** Accepts the explanation and the cause of the error. */
    public ClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
