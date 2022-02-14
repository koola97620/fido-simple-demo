package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.common.crypto.COSEAlgorithmIdentifier;
import com.example.fidosimpledemo.common.crypto.ChallengeGenerator;
import com.example.fidosimpledemo.common.crypto.ServerConstant;
import com.example.fidosimpledemo.fidoserver.domain.UserKey;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2RpNotFoundException;
import com.example.fidosimpledemo.fidoserver.exception.InternalErrorCode;
import com.example.fidosimpledemo.rpserver.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CreateChallengeService {

    private final RpService rpService;
    private final UserKeyService userKeyService;
    private final SessionService sessionService;

    public CreateChallengeService(RpService rpService, UserKeyService userKeyService, SessionService sessionService) {
        this.rpService = rpService;
        this.userKeyService = userKeyService;
        this.sessionService = sessionService;
    }

    public RegOptionResponse getChallenge(RegOptionRequest regOptionRequest) {
        String rpId = regOptionRequest.getRp().getId();
        if (!rpService.containsRp(rpId)) {
            throw new FIDO2RpNotFoundException(rpId);
        }
        String userId = regOptionRequest.getUser().getId();

        List<UserKey> userKeys = userKeyService.getUserKeyByRpIdAndUserId(rpId, userId);

        Session session = sessionService.createSessionData();
        //rpService.findByName(rpName);

        RegOptionResponse response =
                RegOptionResponse.builder()
                        .rp(regOptionRequest.getRp())
                        .user(regOptionRequest.getUser())
                        .excludeCredentials(getExcludeAndIncludeCredentials(userKeys))
                        .pubKeyCredParams(createPubkeyCredParams())
                        .challenge(ChallengeGenerator.generate(ServerConstant.SERVER_CHALLENGE_LENGTH))
                        .timeout(180000L)
                        .authenticatorSelection(regOptionRequest.getAuthenticatorSelection())
                        .attestation(regOptionRequest.getAttestation())
                        .sessionId(session.getId())
                        .serverResponse(
                                ServerResponse.builder()
                                        .internalErrorCode(InternalErrorCode.SUCCESS.getCode())
                                        .internalError(InternalErrorCode.SUCCESS.name())
                                        .build()
                        )
                        .build();

        session.setRegOptionResponse(response);
        sessionService.save(session);

        return response;
    }

    private List<PublicKeyCredentialParameters> createPubkeyCredParams() {
        List<PublicKeyCredentialParameters> publicKeyCredentialParameters = new ArrayList<>();
        for (COSEAlgorithmIdentifier identifier : COSEAlgorithmIdentifier.values()) {
            PublicKeyCredentialParameters parameters = new PublicKeyCredentialParameters();
            parameters.setType(PublicKeyCredentialType.PUBLIC_KEY);
            parameters.setAlg(identifier);
            publicKeyCredentialParameters.add(parameters);
        }
        return publicKeyCredentialParameters;
    }

    private List<ServerPublicKeyCredentialDescriptor> getExcludeAndIncludeCredentials(List<UserKey> userKeys) {
        List<ServerPublicKeyCredentialDescriptor> publicKeyCredentialDescriptors = new ArrayList<>();
        if (userKeys != null &&
                !userKeys.isEmpty()) {
            for (UserKey userKey : userKeys) {
                ServerPublicKeyCredentialDescriptor serverPublicKeyCredentialDescriptor =
                        ServerPublicKeyCredentialDescriptor.builder()
                                .id(userKey.getCredentialId())
                                .type(PublicKeyCredentialType.PUBLIC_KEY)
                                .transports(userKey.getTransports())
                                .build();
                publicKeyCredentialDescriptors.add(serverPublicKeyCredentialDescriptor);
            }
        }

        return publicKeyCredentialDescriptors;
    }
}
