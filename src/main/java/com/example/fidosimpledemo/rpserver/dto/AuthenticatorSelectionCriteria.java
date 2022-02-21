package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.fidoserver.domain.AuthenticatorAttachment;
import com.example.fidosimpledemo.rpserver.domain.UserVerificationRequirement;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticatorSelectionCriteria {
    private AuthenticatorAttachment authenticatorAttachment;
    private boolean requireResidentKey;
    private UserVerificationRequirement userVerification;
}
