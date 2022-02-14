package com.example.fidosimpledemo.fidoserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum InternalErrorCode {
    SUCCESS(0)
    ;


    @Getter
    private final int code;

    public static InternalErrorCode fromCode(int code) {
        return Arrays
                .stream(InternalErrorCode.values()).filter(e -> e.code == code)
                .findFirst()
                .get();
    }

}
