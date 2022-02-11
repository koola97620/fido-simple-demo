package com.example.fidosimpledemo.rpserver.api;

import com.example.fidosimpledemo.rpserver.app.ChallengeService;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsRequest;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AdapterApi {

    private final ChallengeService challengeService;

    public AdapterApi(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/rp/attestation/options")
    public ServerPublicKeyCredentialCreationOptionsResponse getRegistrationChallenge(
            @RequestHeader String host,
            @RequestBody ServerPublicKeyCredentialCreationOptionsRequest request,
            HttpServletResponse httpServletResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        challengeService.getChallenge(host,request,httpHeaders);
        return null;
    }
}
