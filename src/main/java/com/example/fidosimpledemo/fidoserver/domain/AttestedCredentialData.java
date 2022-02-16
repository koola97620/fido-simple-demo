package com.example.fidosimpledemo.fidoserver.domain;

import lombok.Data;

@Data
public class AttestedCredentialData {
    private byte[] aaguid;
    private byte[] credentialId;
    private CredentialPublicKey credentialPublicKey;
}
