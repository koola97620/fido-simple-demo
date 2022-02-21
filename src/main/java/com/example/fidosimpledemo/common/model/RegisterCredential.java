package com.example.fidosimpledemo.common.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RegisterCredential {
    @NotNull
    @Valid
    private ServerRegPublicKeyCredential serverPublicKeyCredential;
    @NotBlank
    private String sessionId;
    @NotBlank
    private String origin;
    @NotBlank
    private String rpId;
    private TokenBinding tokenBinding;
}
