package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

public class ServerPublicKeyCredentialDescriptor {
    private PublicKeyCredentialType type;
    private String id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AuthenticatorTransport> transports;

    @Builder
    public ServerPublicKeyCredentialDescriptor(PublicKeyCredentialType type, String id, List<AuthenticatorTransport> transports) {
        this.type = type;
        this.id = id;
        this.transports = transports;
    }
}
