package com.example.fidosimpledemo.fidoserver.infra;

import com.example.fidosimpledemo.fidoserver.app.AttestationVerificationResult;
import com.example.fidosimpledemo.fidoserver.app.AttestationVerifier;
import com.example.fidosimpledemo.fidoserver.domain.AttestationStatementFormatIdentifier;
import com.example.fidosimpledemo.fidoserver.util.AuthenticatorData;
import com.example.fidosimpledemo.fidoserver.domain.AttestationStatement;
import com.example.fidosimpledemo.common.model.AttestationType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class NoneAttestationVerifier implements AttestationVerifier {
    @Override
    public AttestationStatementFormatIdentifier getIdentifier() {
        return AttestationStatementFormatIdentifier.NONE;
    }

    @Override
    public AttestationVerificationResult verify(AttestationStatement attestationStatement, AuthenticatorData authenticatorData,
                                                byte[] clientDataHash) {
        return AttestationVerificationResult
                .builder()
                .success(true)
                .type(AttestationType.NONE)
                .trustPath(new ArrayList<>())
                .format(AttestationStatementFormatIdentifier.NONE)
                .build();

    }


}
