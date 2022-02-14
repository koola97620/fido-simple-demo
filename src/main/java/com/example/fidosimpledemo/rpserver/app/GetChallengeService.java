package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.query.RpQueryService;
import com.example.fidosimpledemo.rpserver.dto.*;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.infra.FidoApiClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GetChallengeService {

    private final RpQueryService rpQueryService;
    private final FidoApiClient fidoApiClient;

    public GetChallengeService(RpQueryService rpQueryService, FidoApiClient fidoApiClient) {
        this.rpQueryService = rpQueryService;
        this.fidoApiClient = fidoApiClient;
    }

    public GetChallengeDto getChallenge(String host, ServerPublicKeyCredentialCreationOptionsRequest request) {
        RpEntity rpEntity = rpQueryService.getRpEntity(host);
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

    private RegOptionRequest createFidoRequest(RpEntity rpEntity, ServerPublicKeyCredentialCreationOptionsRequest request) {
        PublicKeyCredentialRpEntity rp = new PublicKeyCredentialRpEntity();
        rp.setId(rpEntity.getId());
        rp.setName(rpEntity.getName());
        rp.setIcon(rpEntity.getIcon());

        ServerPublicKeyCredentialUserEntity user = ServerPublicKeyCredentialUserEntity.of(request);
        user.setName(request.getUsername());
        return RegOptionRequest.builder()
                .rp(rp)
                .user(user)
                .authenticatorSelection(request.getAuthenticatorSelection())
                .attestation(request.getAttestation())
                .credProtect(request.getCredProtect())
                .build();
    }

}
