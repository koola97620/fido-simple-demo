package com.example.fidosimpledemo.rpserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerPublicKeyCredentialGetOptionsResponseDto {
    private ServerPublicKeyCredentialGetOptionsResponse response;
    private String sessionId;
}
