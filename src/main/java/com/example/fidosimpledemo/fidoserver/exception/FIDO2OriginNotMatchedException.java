package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2OriginNotMatchedException extends RuntimeException {
    public FIDO2OriginNotMatchedException(String message) {
        super(message);
    }
}
