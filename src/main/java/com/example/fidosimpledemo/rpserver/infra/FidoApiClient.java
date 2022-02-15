package com.example.fidosimpledemo.rpserver.infra;

import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "rp", url = "http://localhost:${server.port}")
public interface FidoApiClient {

    @PostMapping("/fido2/req/challenge")
    @Headers(value = {})
    RegOptionResponse createChallenge(RegOptionRequest request);

}
