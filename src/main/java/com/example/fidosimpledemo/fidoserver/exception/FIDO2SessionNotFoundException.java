package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2SessionNotFoundException extends RuntimeException {
    public FIDO2SessionNotFoundException(String message) {
        super(message);
    }
}
