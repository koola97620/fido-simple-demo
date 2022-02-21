package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorTransport;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPublicKeyCredentialDescriptor {
    private PublicKeyCredentialType type;
    private String id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AuthenticatorTransport> transports;
}
