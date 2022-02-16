package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2DuplicateCredentialIdException extends RuntimeException {
    public FIDO2DuplicateCredentialIdException(String message) {
        super(message);
    }
}
