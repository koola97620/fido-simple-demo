package com.example.fidosimpledemo.rpserver.dto;

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
public class ServerPublicKeyCredentialGetOptionsResponse extends AdapterServerResponse{
    private String challenge;
    private long timeout;
    private String rpId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ServerPublicKeyCredentialDescriptor> allowCredentials;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private UserVerificationRequirement userVerification;
    //extensions
    private AuthenticationExtensionsClientInputs extensions;
}
