package com.example.fidosimpledemo.fidoserver.domain;

import com.example.fidosimpledemo.rpserver.dto.AttestationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.NamedAttributeNode;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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
