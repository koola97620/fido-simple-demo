package com.example.fidosimpledemo.rpserver.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class AuthOptionRequest {
    @NotBlank
    private String rpId;
    private String userId;
    //private UserVerificationRequirement userVerification;
}
