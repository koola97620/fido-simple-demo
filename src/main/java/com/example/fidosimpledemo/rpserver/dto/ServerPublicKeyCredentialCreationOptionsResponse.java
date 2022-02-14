package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPublicKeyCredentialCreationOptionsResponse extends AdapterServerResponse{
    private PublicKeyCredentialRpEntity rp;
    private ServerPublicKeyCredentialUserEntity user;
    private String challenge;
    private List<PublicKeyCredentialParameters> pubKeyCredParams;
    private long timeout;
    private List<ServerPublicKeyCredentialDescriptor> excludeCredentials;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthenticatorSelectionCriteria authenticatorSelection;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AttestationConveyancePreference attestation;
    //extensions
    private AuthenticationExtensionsClientInputs extensions;
}
