package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Data
public class ServerAuthenticatorResponse {
    @NotBlank
    @Base64Encoded
    private String clientDataJSON;  // base64url encoded
}
