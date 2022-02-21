package com.example.fidosimpledemo.rpserver.infra;

import com.example.fidosimpledemo.common.model.*;
import com.example.fidosimpledemo.rpserver.dto.*;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "rp", url = "http://localhost:${server.port}")
public interface FidoApiClient {

    @PostMapping("/fido2/req/challenge")
    @Headers(value = {})
    RegOptionResponse createChallenge(RegOptionRequest request);

    @PostMapping("/fido2/req/response")
    @Headers(value = {})
    RegisterCredentialResult sendRegistrationResponse(RegisterCredential registerCredential);

    @PostMapping("/fido2/auth/challenge")
    @Headers(value = {})
    AuthOptionResponse getAuthChallenge(AuthOptionRequest authOptionRequest);

    @PostMapping("/fido2/auth/response")
    @Headers(value = {})
    VerifyCredentialResult sendAuthenticationResponse(VerifyCredentialRequest verifyCredentialRequest);
}
