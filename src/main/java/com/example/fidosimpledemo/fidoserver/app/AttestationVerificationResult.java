package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.AttestationStatementFormatIdentifier;
import com.example.fidosimpledemo.common.model.AttestationType;
import lombok.Builder;
import lombok.Data;

import java.security.cert.Certificate;
import java.util.List;

@Data
@Builder
public class AttestationVerificationResult {
    private boolean success;
    private AttestationType type;
    private List<Certificate> trustPath;
    private byte[] ecdaaKeyId;
    private AttestationStatementFormatIdentifier format;
}
