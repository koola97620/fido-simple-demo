package com.example.fidosimpledemo.fidoserver.domain;

import com.example.fidosimpledemo.common.model.AttestationType;
import lombok.*;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserKey {
    private String rpId;
    private String userId;
    private String userName;
    private String icon;
    private String aaguid;
    private String credentialId;
    private PublicKey publicKey;
    private COSEAlgorithm algorithm;
    private Long signCounter;
    private AttestationType attestationType;
    private List<AuthenticatorTransport> transports;
    private Boolean rk;
    private LocalDateTime createdAt;
    private LocalDateTime lastAuthenticatedAt;
}
