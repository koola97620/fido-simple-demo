package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;

@Data
public class AdapterAuthServerPublicKeyCredential extends Credential{
    private String rawId;
    private ServerAuthenticatorAssertionResponse response;
    // extension
    private AuthenticationExtensionsClientOutputs extensions;
}
