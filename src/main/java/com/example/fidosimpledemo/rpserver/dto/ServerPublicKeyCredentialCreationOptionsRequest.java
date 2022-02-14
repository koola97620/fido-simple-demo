package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class ServerPublicKeyCredentialCreationOptionsRequest {
    private String username;
    private String displayName;
    private AuthenticatorSelectionCriteria authenticatorSelection;
    private AttestationConveyancePreference attestation = AttestationConveyancePreference.none;
    private CredProtect credProtect;
}
