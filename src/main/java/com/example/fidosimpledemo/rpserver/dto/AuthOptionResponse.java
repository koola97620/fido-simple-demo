package com.example.fidosimpledemo.rpserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@ToString
public class AuthOptionResponse implements ServerAPIResult{
    private ServerResponse serverResponse;
    private String challenge;
    private long timeout;
    private String rpId;
    private List<ServerPublicKeyCredentialDescriptor> allowCredentials;
    private UserVerificationRequirement userVerification;
    private String sessionId;
    // extension
    private AuthenticationExtensionsClientInputs extensions;
}
