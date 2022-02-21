package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.rpserver.domain.CredentialProtectionPolicy;
import lombok.Data;

@Data
public class CredProtect {
    private CredentialProtectionPolicy credentialProtectionPolicy;
    private Boolean enforceCredentialProtectionPolicy;
}
