package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.query.RpQueryService;
import com.example.fidosimpledemo.rpserver.api.RegisterCredential;
import com.example.fidosimpledemo.rpserver.api.ServerRegPublicKeyCredential;
import com.example.fidosimpledemo.rpserver.dto.*;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.infra.FidoApiClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class RpAttestationService {

    private final RpQueryService rpQueryService;
    private final FidoApiClient fidoApiClient;

    public RpAttestationService(RpQueryService rpQueryService, FidoApiClient fidoApiClient) {
        this.rpQueryService = rpQueryService;
        this.fidoApiClient = fidoApiClient;
    }

    public GetChallengeDto getChallenge(String host, ServerPublicKeyCredentialCreationOptionsRequest request) {
        RpEntity rpEntity = getRpEntity(host);
        RegOptionRequest fidoRequest = createFidoRequest(rpEntity, request);

        RegOptionResponse response = fidoApiClient.createChallenge(fidoRequest);

        ServerPublicKeyCredentialCreationOptionsResponse serverResponse = ServerPublicKeyCredentialCreationOptionsResponse.builder()
                .rp(response.getRp())
                .user(response.getUser())
                .attestation(response.getAttestation())
                .authenticatorSelection(response.getAuthenticatorSelection())
                .challenge(response.getChallenge())
                .excludeCredentials(response.getExcludeCredentials())
                .pubKeyCredParams(response.getPubKeyCredParams())
                .timeout(response.getTimeout())
                .extensions(response.getExtensions())
                .build();
        serverResponse.setStatus(Status.OK);

        return GetChallengeDto.builder()
                .response(serverResponse)
                .sessionId(response.getSessionId())
                .build();
    }

    public AdapterServerResponse sendRegistration(String host, String sessionId, String origin, AdapterRegServerPublicKeyCredential clientResponse) {
        RpEntity rpEntity = getRpEntity(host);

        RegisterCredential registerCredential = new RegisterCredential();
        ServerRegPublicKeyCredential serverRegPublicKeyCredential = new ServerRegPublicKeyCredential();
        serverRegPublicKeyCredential.setId(clientResponse.getId());
        serverRegPublicKeyCredential.setType(clientResponse.getType());
        serverRegPublicKeyCredential.setResponse(clientResponse.getResponse());
        serverRegPublicKeyCredential.setExtensions(clientResponse.getExtensions());
        registerCredential.setServerPublicKeyCredential(serverRegPublicKeyCredential);
        registerCredential.setRpId(rpEntity.getId());
        registerCredential.setSessionId(sessionId);
        registerCredential.setOrigin(origin);

        fidoApiClient.sendRegistrationResponse(registerCredential);

        AdapterServerResponse response = new AdapterServerResponse();
        response.setStatus(Status.OK);
        return response;
    }

    private RpEntity getRpEntity(String host) {
        return rpQueryService.getRpEntity(host);
    }

    private RegOptionRequest createFidoRequest(RpEntity rpEntity, ServerPublicKeyCredentialCreationOptionsRequest request) {
        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity();
        rp.setId(rpEntity.getId());
        rp.setName(rpEntity.getName());
        rp.setIcon(rpEntity.getIcon());

        ServerPublicKeyCredentialUserEntity user =
                ServerPublicKeyCredentialUserEntity.of(createUserId(request.getUsername()), request.getDisplayName());
        user.setName(request.getUsername());
        return RegOptionRequest.builder()
                .rp(rp)
                .user(user)
                .authenticatorSelection(request.getAuthenticatorSelection())
                .attestation(request.getAttestation())
                .credProtect(request.getCredProtect())
                .build();
    }

    private String createUserId(String username) {
        if (ObjectUtils.isEmpty(username)) {
            return null;
        }

        byte[] digest = Digests.sha256(username.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
