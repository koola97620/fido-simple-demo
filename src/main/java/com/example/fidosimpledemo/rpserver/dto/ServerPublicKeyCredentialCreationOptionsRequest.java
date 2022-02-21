package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.rpserver.domain.AttestationConveyancePreference;
import lombok.Data;

@Data
public class ServerPublicKeyCredentialCreationOptionsRequest {
    private String username;
    private String displayName;
    private AuthenticatorSelectionCriteria authenticatorSelection;
    private AttestationConveyancePreference attestation = AttestationConveyancePreference.none;
    private CredProtect credProtect;
}
