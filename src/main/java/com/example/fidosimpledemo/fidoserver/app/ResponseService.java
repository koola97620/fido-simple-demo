package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.domain.*;
import com.example.fidosimpledemo.fidoserver.exception.*;
import com.example.fidosimpledemo.rpserver.api.ServerRegPublicKeyCredential;
import com.example.fidosimpledemo.rpserver.api.TokenBinding;
import com.example.fidosimpledemo.rpserver.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class ResponseService {

    private final SessionService sessionService;
    private final AttestationService attestationService;
    private final UserKeyService userKeyService;

    public ResponseService(SessionService sessionService, AttestationService attestationService, UserKeyService userKeyService) {
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

        return getRegisterCredentialResult(
                session.getRegOptionResponse(), attestationResponse.getTransports(),
                attestationObject.getAuthData(), attestationVerificationResult,
                serverPublicKeyCredential.getExtensions(), rpId
        );
    }

    @Transactional
    public VerifyCredentialResult handleAssertion(ServerAuthPublicKeyCredential serverPublicKeyCredential, String sessionId,
                                                  String origin, String rpId, TokenBinding tokenBinding) {
        Session session = sessionService.getSession(sessionId);
        ServerAuthenticatorAssertionResponse assertionResponse = serverPublicKeyCredential.getResponse();

        ClientDataTranslator.handleClientDataJSON(
                "webauthn.get", session.getAuthOptionResponse().getChallenge(), assertionResponse.getClientDataJSON(), origin, tokenBinding);
        byte[] authDataBytes = Base64.getUrlDecoder().decode(serverPublicKeyCredential.getResponse().getAuthenticatorData());
        AuthenticatorData authData = getAuthData(authDataBytes);

        checkCredentialId(serverPublicKeyCredential, session);

        UserKey userKey = getUserKey(serverPublicKeyCredential, rpId);

        verifyUserHandle(serverPublicKeyCredential, userKey);
        verifyAuthDataValues(rpId, session, authData, userKey.getAaguid());
        verifySignature(serverPublicKeyCredential, authDataBytes, userKey);

        checkSignCounter(authData, userKey);
        log.info("[Finish handling assertion]");
        return createVerifyCredentialResult(authData, userKey);
    }

    private VerifyCredentialResult createVerifyCredentialResult(AuthenticatorData authData, UserKey userKey) {
        return VerifyCredentialResult
                .builder()
                .aaguid(userKey.getAaguid())
                .userId(userKey.getUserId())
                .userVerified(authData.isUserVerified())
                .userPresent(authData.isUserPresent())
                .serverResponse(ServerResponse
                        .builder()
                        .internalErrorCode(InternalErrorCode.SUCCESS.getCode())
                        .internalError(InternalErrorCode.SUCCESS.name())
                        .build())
                .build();
    }

    private void checkSignCounter(AuthenticatorData authData, UserKey userKey) {
        // check signature counter
        log.info("Check signature counter");
        if (authData.getSignCount() != 0 || userKey.getSignCounter() != 0) {
            if (authData.getSignCount() > userKey.getSignCounter()) {
                // update
                userKey.setSignCounter(authData.getSignCount());
                userKeyService.update(userKey);
            } else {
                throw new FIDO2VerifyException("ASSERTION_SIGNATURE_VERIFICATION_FAIL");
                // authenticator is may cloned, reject.
            }
        }
    }

    private void verifySignature(ServerAuthPublicKeyCredential serverPublicKeyCredential, byte[] authDataBytes, UserKey userKey) {
        // prepare toBeSignedMessage
        log.info("Prepare toBeSignedMessage (authData + hash(cData))");
        byte[] cData = Base64.getUrlDecoder().decode(serverPublicKeyCredential.getResponse().getClientDataJSON());
        byte[] hash = Digests.sha256(cData);
        // binary concat of authData and hash
        int toBeSignedMessageSize = authDataBytes.length + hash.length;
        byte[] toBeSignedMessage = ByteBuffer
                .allocate(toBeSignedMessageSize)
                .put(authDataBytes)
                .put(hash)
                .array();

        // verify signature
        log.info("Verify signature");
        byte[] signatureBytes = Base64.getUrlDecoder().decode(serverPublicKeyCredential.getResponse().getSignature());
        boolean result = SignatureHelper.verifySignature(userKey.getPublicKey(), toBeSignedMessage, signatureBytes, userKey.getAlgorithm());
        if (!result) {
            throw new FIDO2VerifyException("ASSERTION_SIGNATURE_VERIFICATION_FAIL:" + "Signature verification failed:" + userKey.getAaguid());
        }
    }

    private void verifyAuthDataValues(String rpId, Session session, AuthenticatorData authData, String aaguid) {
        // verify RP ID (compare with SHA256 hash or RP ID)
        log.info("Verify hash of RP ID with rpIdHash in authData");

        byte[] rpIdHash = Digests.sha256(rpId.getBytes(StandardCharsets.UTF_8));
        if (!Arrays.equals(authData.getRpIdHash(), rpIdHash)) {
            throw new FIDO2VerifyException("RPID_HASH_NOT_MATCHED:" + "RP ID hash is not matched" + aaguid);
        }

        // verify user present flag
        log.info("Verify user present flag. Should be set");
        if (!authData.isUserPresent()) {
            throw new FIDO2VerifyException("USER_PRESENCE_FLAG_NOT_SET:aaguId:" + aaguid);
        }

        // verify user verification
//        log.info("Verify user verification flag if user verification required");
//        if (session.getAuthOptionResponse().getUserVerification() != null &&
//                session.getAuthOptionResponse().getUserVerification() == UserVerificationRequirement.REQUIRED &&
//                !authData.isUserVerified()) {
//            throw new FIDO2VerifyException(InternalErrorCode.USER_VERIFICATION_FLAG_NOT_SET, "User verification flag not set", aaguid);
//        }
    }

    private void verifyUserHandle(ServerAuthPublicKeyCredential serverPublicKeyCredential, UserKey userKey) {
        // check userHandle if it present
        log.info("Check userHandle if it is present, user handle MUST be identical to user id of a founded credential");
        if (!ObjectUtils.isEmpty(serverPublicKeyCredential.getResponse().getUserHandle())) {
            if (!userKey.getUserId().equals(serverPublicKeyCredential.getResponse().getUserHandle())) {
                // MUST identical to uerHandle
                throw new FIDO2VerifyException("USER_HANDLE_NOT_MATCHED:aaguId:" + userKey.getAaguid());
            }
        }
    }

    private AuthenticatorData getAuthData(byte[] authDataBytes) {
        AuthenticatorData authData;
        try {
            authData = AuthenticatorData.decode(authDataBytes);
        } catch (IOException e) {
            throw new FIDO2InvalidAuthenticatorDataException("INVALID_AUTHENTICATOR_DATA");
        }
        return authData;
    }

    protected UserKey getUserKey(ServerAuthPublicKeyCredential serverPublicKeyCredential, String rpId) {
        log.info("Get user key with rpId and credential id");
        return userKeyService.getWithCredentialId(rpId, serverPublicKeyCredential.getId());
    }


    private RegisterCredentialResult getRegisterCredentialResult(
            RegOptionResponse regOptionResponse, List<AuthenticatorTransport> transports,
            AuthenticatorData authData, AttestationVerificationResult attestationVerificationResult,
            AuthenticationExtensionsClientOutputs clientExtensions, String rpId) {
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

        userKeyService.saveUser(userKey);

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

    private void checkCredentialId(ServerAuthPublicKeyCredential serverPublicKeyCredential, Session session) {
        // check credential.id is in the allow credential list (if we set the allow credential list)
        log.debug("credential ID: {}", serverPublicKeyCredential.getId());
        log.info("Check credential id in response is in the allow credential list");
        boolean credentialIdFound = false;
        if (!session.getAuthOptionResponse().getAllowCredentials().isEmpty()) {
            for (ServerPublicKeyCredentialDescriptor publicKeyCredentialDescriptor : session.getAuthOptionResponse().getAllowCredentials()) {
                if (publicKeyCredentialDescriptor.getId().equals(serverPublicKeyCredential.getId())) {
                    credentialIdFound = true;
                    break;
                }
            }
            if (!credentialIdFound) {
                throw new FIDO2VerifyException("CREDENTIAL_ID_NOT_FOUND");
            }
        }
    }
}
