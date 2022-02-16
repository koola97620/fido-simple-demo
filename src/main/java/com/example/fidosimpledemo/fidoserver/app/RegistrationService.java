package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.*;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2DuplicateCredentialIdException;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2InvalidAttestationException;
import com.example.fidosimpledemo.fidoserver.exception.InternalErrorCode;
import com.example.fidosimpledemo.rpserver.api.ServerRegPublicKeyCredential;
import com.example.fidosimpledemo.rpserver.api.TokenBinding;
import com.example.fidosimpledemo.rpserver.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class RegistrationService {

    private final SessionService sessionService;
    private final AttestationService attestationService;
    private final UserKeyService userKeyService;

    public RegistrationService(SessionService sessionService, AttestationService attestationService, UserKeyService userKeyService) {
        this.sessionService = sessionService;
        this.attestationService = attestationService;
        this.userKeyService = userKeyService;
    }

    public RegisterCredentialResult regist(ServerRegPublicKeyCredential serverPublicKeyCredential, String sessionId, String origin, String rpId, TokenBinding tokenBinding) {
        Session session = sessionService.getSession(sessionId);
        ServerAuthenticatorAttestationResponse attestationResponse = serverPublicKeyCredential.getResponse();

        byte[] clientDataHsh = ClientDataTranslator.handleClientDataJSON(
                "webauthn.create", session.getRegOptionResponse().getChallenge(), attestationResponse.getClientDataJSON(), origin, tokenBinding);

        AttestationObject attestationObject = attestationService.getAttestationObject(attestationResponse);
        attestationService.attestationObjectValidationCheck(rpId, session.getRegOptionResponse().getAuthenticatorSelection(), attestationObject);
        AttestationVerificationResult attestationVerificationResult = attestationService.verifyAttestation(clientDataHsh, attestationObject);

        // prepare trust anchors, attestation fmt (from metadata service or trusted source)
        if (!attestationVerificationResult.isSuccess()) {
            throw new FIDO2InvalidAttestationException("ATTESTATION_SIGNATURE_VERIFICATION_FAIL");
        }

//        if (attestationVerificationResult.getType() != AttestationType.SELF && attestationVerificationResult.getType() != AttestationType.NONE) {
//            attestationService.verifyAttestationCertificate(attestationObject, attestationVerificationResult);
//        }

        return getRegisterCredentialResult(session.getRegOptionResponse(), attestationResponse.getTransports(), attestationObject.getAuthData(), attestationVerificationResult, serverPublicKeyCredential.getExtensions(), rpId);

    }

    private RegisterCredentialResult getRegisterCredentialResult(RegOptionResponse regOptionResponse, List<AuthenticatorTransport> transports, AuthenticatorData authData, AttestationVerificationResult attestationVerificationResult, AuthenticationExtensionsClientOutputs clientExtensions, String rpId) {
        // get credential info
        log.info("Get public key credential info");
        AttestedCredentialData attestedCredentialData = authData.getAttestedCredentialData();
        String credentialId = Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(attestedCredentialData.getCredentialId());
        log.info("Convert COSE public key to java public key instance");

        // check credential id is duplicated by users
        // if the duplications are exist, we may reject or deleting old registration and registering new one
        log.info("Check duplication of credential id in permanent storage");
        if (userKeyService.containsCredential(rpId, credentialId)) {
            // just reject
            throw new FIDO2DuplicateCredentialIdException("DUPLICATED_CREDENTIAL_ID:" + credentialId);
        }

        // store registration for latter authentication
        log.info("Store public key credential info in permanent storage");

        UserKey userKey = ExtensionHelper.createUserKeyWithExtensions(UserKey
                .builder()
                .publicKey(CredentialPublicKeyHelper.convert(attestedCredentialData.getCredentialPublicKey()))
                .aaguid(AaguidUtil.convert(attestedCredentialData.getAaguid()))
                .credentialId(credentialId)
                .userId(regOptionResponse.getUser().getId())
                .userName(regOptionResponse.getUser().getName())
                .rpId(regOptionResponse.getRp().getId())
                .algorithm(CredentialPublicKeyHelper.getCOSEAlgorithm(attestedCredentialData.getCredentialPublicKey()))
                .signCounter(authData.getSignCount())
                .attestationType(attestationVerificationResult.getType())
                .transports(transports), clientExtensions, authData.getExtensions());

        userKeyService.createUser(userKey);

        // return registration processing result
        log.info("[Finish handling attestation]");
        return createRegisterCredentialResult(authData, attestedCredentialData, credentialId, userKey);
    }

    protected RegisterCredentialResult createRegisterCredentialResult(AuthenticatorData authData, AttestedCredentialData attestedCredentialData, String credentialId, UserKey userKey) {
        return RegisterCredentialResult
                .builder()
                .aaguid(AaguidUtil.convert(attestedCredentialData.getAaguid()))
                .credentialId(credentialId)
                .attestationType(userKey.getAttestationType())
                .authenticatorTransports(userKey.getTransports())
                .userVerified(authData.isUserVerified())
                .rk(userKey.getRk())
                //.credProtect(userKey.getCredProtect())
                .serverResponse(ServerResponse
                        .builder()
                        .internalErrorCode(InternalErrorCode.SUCCESS.getCode())
                        .internalError(InternalErrorCode.SUCCESS.name())
                        .build())
                .build();
    }
}
