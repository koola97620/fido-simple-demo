package com.example.fidosimpledemo.rpserver.dto;

import lombok.Data;

@Data
public class CredProtect {
    private CredentialProtectionPolicy credentialProtectionPolicy;
    private Boolean enforceCredentialProtectionPolicy;
}
