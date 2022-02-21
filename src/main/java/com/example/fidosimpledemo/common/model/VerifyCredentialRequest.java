package com.example.fidosimpledemo.common.model;

import com.example.fidosimpledemo.common.model.TokenBinding;
import com.example.fidosimpledemo.rpserver.dto.ServerAuthPublicKeyCredential;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class VerifyCredentialRequest {
    @NotNull
    @Valid
    private ServerAuthPublicKeyCredential serverPublicKeyCredential;
    @NotBlank
    private String sessionId;
    @NotBlank
    private String origin;
    @NotBlank
    private String rpId;
    private TokenBinding tokenBinding;
}
