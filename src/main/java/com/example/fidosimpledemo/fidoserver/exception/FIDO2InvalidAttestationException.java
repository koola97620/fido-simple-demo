package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2InvalidAttestationException extends RuntimeException {
    public FIDO2InvalidAttestationException(String message) {
        super(message);
    }
}
