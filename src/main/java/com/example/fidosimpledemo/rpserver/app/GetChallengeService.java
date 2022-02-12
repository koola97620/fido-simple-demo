package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsRequest;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.rpserver.domain.Rp;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.infra.FidoApiClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class GetChallengeService {

    private final RpService rpService;
    private final FidoApiClient fidoApiClient;

    public GetChallengeService(RpService rpService, FidoApiClient fidoApiClient) {
        this.rpService = rpService;
        this.fidoApiClient = fidoApiClient;
    }

    public void getChallenge(String host, ServerPublicKeyCredentialCreationOptionsRequest request, HttpHeaders httpHeaders) {
        Rp rpEntity = rpService.getRpEntity(host);
        RegOptionRequest fidoRequest = createFidoRequest(rpEntity, request);
        RegOptionResponse response = fidoApiClient.createChallenge(fidoRequest);

    }

    private RegOptionRequest createFidoRequest(Rp rpEntity, ServerPublicKeyCredentialCreationOptionsRequest request) {
        PublicKeyCredentialRpEntity rp = PublicKeyCredentialRpEntity.of(rpEntity);
        ServerPublicKeyCredentialUserEntity user = ServerPublicKeyCredentialUserEntity.of(request);
        return RegOptionRequest.builder()
                .rp(rp)
                .user(user)
                .build();
    }

}
