package com.example.fidosimpledemo.rpserver.api;

import com.example.fidosimpledemo.rpserver.dto.AuthenticationExtensionsClientOutputs;
import com.example.fidosimpledemo.rpserver.dto.Credential;
import com.example.fidosimpledemo.rpserver.dto.ServerAuthenticatorAttestationResponse;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ServerRegPublicKeyCredential extends Credential {
    @NotNull
    @Valid
    private ServerAuthenticatorAttestationResponse response;
    // extension
    private AuthenticationExtensionsClientOutputs extensions;
}
