package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.AttestationStatementFormatIdentifier;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AttestationVerifierFactory {
    private final Map<AttestationStatementFormatIdentifier, AttestationVerifier> verifierMap;

    public AttestationVerifierFactory(List<AttestationVerifier> verifierList) {
        verifierMap = verifierList.stream()
                .collect(Collectors.toMap(AttestationVerifier::getIdentifier, Function.identity()));
    }

    public AttestationVerifier getVerifier(AttestationStatementFormatIdentifier identifier) {
        return verifierMap.get(identifier);
    }
}
