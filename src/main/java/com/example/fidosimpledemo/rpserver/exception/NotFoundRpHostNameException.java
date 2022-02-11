package com.example.fidosimpledemo.rpserver.exception;

public class NotFoundRpHostNameException extends RuntimeException {
    public NotFoundRpHostNameException() {
        super("등록된 Hostname이 없습니다.");
    }
}
