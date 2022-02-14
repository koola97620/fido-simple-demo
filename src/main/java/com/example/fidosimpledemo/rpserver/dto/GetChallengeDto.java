package com.example.fidosimpledemo.rpserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetChallengeDto {
    private ServerPublicKeyCredentialCreationOptionsResponse response;
    private String sessionId;
}
