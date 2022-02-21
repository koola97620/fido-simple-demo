package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.common.model.Credential;
import lombok.Data;

@Data
public class AdapterAuthServerPublicKeyCredential extends Credential {
    private String rawId;
    private ServerAuthenticatorAssertionResponse response;
    // extension
    private AuthenticationExtensionsClientOutputs extensions;
}
