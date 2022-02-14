package com.example.fidosimpledemo.fidoserver.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum AuthenticatorTransport {
    USB("usb")
    ;

    @JsonValue
    @Getter private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AuthenticatorTransport fromValue(String value) {
        return Arrays.stream(AuthenticatorTransport.values())
                .filter(e -> e.value.equals(value))
                .findFirst()
                .get();
    }

}
