package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.fidoserver.query.RpQueryService;
import com.example.fidosimpledemo.rpserver.dto.*;
import com.example.fidosimpledemo.rpserver.infra.FidoApiClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class RpAssertionService {

    private final RpQueryService rpQueryService;
    private final FidoApiClient fidoApiClient;

    public RpAssertionService(RpQueryService rpQueryService, FidoApiClient fidoApiClient) {
        this.rpQueryService = rpQueryService;
        this.fidoApiClient = fidoApiClient;
    }

    public ServerPublicKeyCredentialGetOptionsResponseDto sendAssertion(String host, ServerPublicKeyCredentialGetOptionsRequest optionRequest) {
        RpEntity rpEntity = rpQueryService.getRpEntity(host);

        AuthOptionRequest authOptionRequest = AuthOptionRequest
                .builder()
                .rpId(rpEntity.getId())
                .userId(createUserId(optionRequest.getUsername()))
//                .userVerification(optionRequest.getUserVerification())
                .build();

        AuthOptionResponse response = fidoApiClient.getAuthChallenge(authOptionRequest);

        ServerPublicKeyCredentialGetOptionsResponse serverResponse = ServerPublicKeyCredentialGetOptionsResponse
                .builder()
                .allowCredentials(response.getAllowCredentials())
                .challenge(response.getChallenge())
                .rpId(response.getRpId())
                .timeout(response.getTimeout())
                //.userVerification(response.getUserVerification())
                .extensions(response.getExtensions())
                .build();

        serverResponse.setStatus(Status.OK);
        return ServerPublicKeyCredentialGetOptionsResponseDto.builder()
                .response(serverResponse)
                .sessionId(response.getSessionId())
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