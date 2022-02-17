package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2UserKeyNotFoundException extends RuntimeException{
    public FIDO2UserKeyNotFoundException(String message) {
        super(message);
    }
}
