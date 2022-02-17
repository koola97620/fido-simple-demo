package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2InvalidAuthenticatorDataException extends RuntimeException {
    public FIDO2InvalidAuthenticatorDataException(String msg) {
        super(msg);
    }
}
