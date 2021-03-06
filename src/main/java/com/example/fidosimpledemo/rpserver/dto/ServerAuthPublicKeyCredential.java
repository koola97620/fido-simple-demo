package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.common.model.Credential;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ServerAuthPublicKeyCredential extends Credential {
    @NotNull
    @Valid
    private ServerAuthenticatorAssertionResponse response;
    // extension
    private AuthenticationExtensionsClientOutputs extensions;
}
