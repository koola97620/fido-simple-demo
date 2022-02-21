package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.domain.AttestationObject;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2InvalidAttestationException;
import com.example.fidosimpledemo.fidoserver.util.AaguidUtil;
import com.example.fidosimpledemo.rpserver.dto.AuthenticatorSelectionCriteria;
import com.example.fidosimpledemo.rpserver.dto.ServerAuthenticatorAttestationResponse;
import com.example.fidosimpledemo.rpserver.domain.UserVerificationRequirement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Service
public class AttestationService {

    private final AttestationVerifierFactory attestationVerifierFactory;

    public AttestationService(AttestationVerifierFactory attestationVerifierFactory) {
        this.attestationVerifierFactory = attestationVerifierFactory;
    }

    public AttestationObject getAttestationObject(ServerAuthenticatorAttestationResponse attestationResponse) {
        byte[] attestationObjectBytes = Base64.getUrlDecoder()
                .decode(attestationResponse.getAttestationObject());

        // perform CBOR decoding
        log.info("Perform CBOR decoding of attestationObject");
        CBORFactory cborFactory = new CBORFactory();
        ObjectMapper objectMapper = new ObjectMapper(cborFactory);
        AttestationObject attestationObject;
        try {
            attestationObject = objectMapper.readValue(attestationObjectBytes, AttestationObject.class);
        } catch (IOException e) {
            throw new FIDO2InvalidAttestationException("INVALID_FORMAT_ATTESTATION_OBJECT" + e.getMessage());
        }
        log.debug("Decoded AttestationObject {}", attestationObject);
        return attestationObject;
    }

    public void attestationObjectValidationCheck(String rpId, AuthenticatorSelectionCriteria authenticatorSelection, AttestationObject attestationObject) {
        // verify attestationObject.authData.attestedCredentialData
        if (attestationObject.getAuthData().getAttestedCredentialData() == null) {
            throw new FIDO2InvalidAttestationException("CREDENTIAL_NOT_INCLUDED");
        }

        // verify RP ID (compare with SHA256 hash or RP ID)
        log.info("Verify hash of RP ID with rpIdHash in authData");
        byte[] rpIdHash = Digests.sha256(rpId.getBytes(StandardCharsets.UTF_8));
        if (!Arrays.equals(attestationObject.getAuthData().getRpIdHash(), rpIdHash)) {
            throw new FIDO2InvalidAttestationException("RPID_HASH_NOT_MATCHED:" + "RP ID hash is not matched:" + AaguidUtil.convert(attestationObject.getAuthData().getAttestedCredentialData().getAaguid()));
        }

        // verify user present flag
        log.info("Verify user present flag. Should be set");
        if (!attestationObject.getAuthData().isUserPresent()) {
            // Temporary comment out for Android chrome testings
//            throw new FIDO2ServerRuntimeException(InternalErrorCode.USER_PRESENCE_FLAG_NOT_SET);
        }

        // verify user verification
        log.info("Verify user verification flag if user verification required");
        if (authenticatorSelection != null &&
                authenticatorSelection.getUserVerification() != null &&
                authenticatorSelection.getUserVerification() == UserVerificationRequirement.REQUIRED &&
                !attestationObject.getAuthData().isUserVerified()) {
            throw new FIDO2InvalidAttestationException("USER_VERIFICATION_FLAG_NOT_SET:" + "User verification flag not set:" + AaguidUtil.convert(attestationObject.getAuthData().getAttestedCredentialData().getAaguid()));
        }
    }

    public AttestationVerificationResult verifyAttestation(byte[] clientDataHsh, AttestationObject attestationObject) {
        // verify attStmt
        log.info("Verify attStmt with format {}", attestationObject.getFmt());
        AttestationVerificationResult attestationVerificationResult =
                attestationVerifierFactory
                        .getVerifier(attestationObject.getFmt())
                        .verify(attestationObject.getAttStmt(), attestationObject.getAuthData(), clientDataHsh);
        log.info("Attestation verification result {}", attestationVerificationResult);
        return attestationVerificationResult;
    }
}
