package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2SessionAlreadyServedException extends RuntimeException {
    public FIDO2SessionAlreadyServedException(String message) {
        super(message);
    }
}
