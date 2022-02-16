package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.fidoserver.app.CreateChallengeService;
import com.example.fidosimpledemo.fidoserver.app.GetAuthChallengeService;
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

    private final CreateChallengeService createChallengeService;
    private final GetAuthChallengeService getAuthChallengeService;

    public ChallengeApi(CreateChallengeService createChallengeService, GetAuthChallengeService getAuthChallengeService) {
        this.createChallengeService = createChallengeService;
        this.getAuthChallengeService = getAuthChallengeService;
    }

    @PostMapping("/fido2/req/challenge")
    public RegOptionResponse getChallenge(@Valid  @RequestBody RegOptionRequest regOptionRequest) {
        return createChallengeService.getChallenge(regOptionRequest);
    }

    @PostMapping("/fido2/auth/challenge")
    public AuthOptionResponse getAuthChallenge(@Valid @RequestBody AuthOptionRequest authOptionRequest) {
        return getAuthChallengeService.getAuthChallenge(authOptionRequest);
    }

}
