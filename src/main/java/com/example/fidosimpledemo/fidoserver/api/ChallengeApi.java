package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.fidoserver.app.ChallengeService;
import com.example.fidosimpledemo.rpserver.dto.AuthOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.AuthOptionResponse;
import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ChallengeApi {

    private final ChallengeService challengeService;

    public ChallengeApi(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/fido2/req/challenge")
    public RegOptionResponse getChallenge(@Valid  @RequestBody RegOptionRequest regOptionRequest) {
        return challengeService.getChallenge(regOptionRequest);
    }

    @PostMapping("/fido2/auth/challenge")
    public AuthOptionResponse getAuthChallenge(@Valid @RequestBody AuthOptionRequest authOptionRequest) {
        return challengeService.getAuthChallenge(authOptionRequest);
    }

}
