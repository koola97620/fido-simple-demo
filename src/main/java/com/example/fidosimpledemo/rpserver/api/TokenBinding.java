package com.example.fidosimpledemo.rpserver.api;

import lombok.Data;

@Data
public class TokenBinding {
    private TokenBindingStatus status;
    private String id;
}
