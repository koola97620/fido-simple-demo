package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.AttestationStatement;
import com.example.fidosimpledemo.fidoserver.domain.AttestationStatementFormatIdentifier;
import com.example.fidosimpledemo.fidoserver.util.AuthenticatorData;

public interface AttestationVerifier {
    AttestationStatementFormatIdentifier getIdentifier();

    AttestationVerificationResult verify(AttestationStatement attestationStatement, AuthenticatorData authenticatorData, byte[] clientDataHash);
}
