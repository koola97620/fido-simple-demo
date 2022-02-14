package com.example.fidosimpledemo.fidoserver.exception;

public class FIDO2RpNotFoundException extends RuntimeException {
    public FIDO2RpNotFoundException(String rpName) {
        super("RP IS NOT FOUND: " + rpName);
    }
}
