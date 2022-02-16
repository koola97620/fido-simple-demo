package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2ChallengeNotMatchedException extends RuntimeException {
    public FIDO2ChallengeNotMatchedException(String message) {
        super(message);
    }
}
