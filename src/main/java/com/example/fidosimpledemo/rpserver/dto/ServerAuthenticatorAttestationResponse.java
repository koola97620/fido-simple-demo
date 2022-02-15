package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorTransport;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class ServerAuthenticatorAttestationResponse extends ServerAuthenticatorResponse {
    @NotBlank
    @Base64Encoded
    private String attestationObject;
    private List<AuthenticatorTransport> transports;    // WebAuthn Level2
}
