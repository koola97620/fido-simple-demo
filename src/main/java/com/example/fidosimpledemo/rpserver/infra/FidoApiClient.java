package com.example.fidosimpledemo.rpserver.infra;

import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "rp", url = "http://localhost:18080")
public interface FidoApiClient {

    @PostMapping("/fido2/req/challenge")
    @Headers("Content-Type: application/json")
    RegOptionResponse createChallenge(RegOptionRequest request);

}
