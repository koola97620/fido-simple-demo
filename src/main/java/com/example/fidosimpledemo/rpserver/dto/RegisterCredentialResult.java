package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.common.model.AttestationType;
import com.example.fidosimpledemo.common.model.ServerResponse;
import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorTransport;
import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorAttachment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class RegisterCredentialResult implements ServerAPIResult {
    private ServerResponse serverResponse;
    private String aaguid;
    private String credentialId;
    private AuthenticatorAttachment authenticatorAttachment;
    private AttestationType attestationType;
    private List<AuthenticatorTransport> authenticatorTransports;   // list of available authenticator transport
    private boolean userVerified;
    private Boolean rk; // RP can decided UX flow by looking at this
    private Integer credProtect;
}
