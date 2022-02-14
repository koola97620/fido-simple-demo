package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.fidoserver.app.CreateChallengeService;
import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ChallengeApi {

    private final CreateChallengeService createChallengeService;

    public ChallengeApi(CreateChallengeService createChallengeService) {
        this.createChallengeService = createChallengeService;
    }

    @PostMapping("/fido2/req/challenge")
    public RegOptionResponse getChallenge(@Valid  @RequestBody RegOptionRequest regOptionRequest) {

        return RegOptionResponse.builder()
                .challenge("new challenge")
                .sessionId("temp session Id")
                .build();
    }
}