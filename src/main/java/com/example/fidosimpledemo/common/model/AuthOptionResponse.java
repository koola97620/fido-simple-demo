package com.example.fidosimpledemo.common.model;

import com.example.fidosimpledemo.common.model.ServerResponse;
import com.example.fidosimpledemo.rpserver.dto.AuthenticationExtensionsClientInputs;
import com.example.fidosimpledemo.rpserver.dto.ServerAPIResult;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialDescriptor;
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
public class AuthOptionResponse implements ServerAPIResult {
    private String challenge;
    private long timeout;
    private String rpId;
    private List<ServerPublicKeyCredentialDescriptor> allowCredentials;
    //private UserVerificationRequirement userVerification;
    private String sessionId;
    private ServerResponse serverResponse;
    // extension
    private AuthenticationExtensionsClientInputs extensions;
}
