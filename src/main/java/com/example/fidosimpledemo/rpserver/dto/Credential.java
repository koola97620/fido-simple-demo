package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Credential {
    @NotBlank
    @Base64Encoded
    private String id;
    @NotNull
    private PublicKeyCredentialType type;
}
