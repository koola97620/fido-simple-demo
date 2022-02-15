package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;

@Data
public class AdapterRegServerPublicKeyCredential extends Credential{
    private String rawId;
    private ServerAuthenticatorAttestationResponse response;
    // extension
    private AuthenticationExtensionsClientOutputs extensions;
}
