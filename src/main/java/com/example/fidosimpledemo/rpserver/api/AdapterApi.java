package com.example.fidosimpledemo.rpserver.api;

import com.example.fidosimpledemo.rpserver.app.GetChallengeService;
import com.example.fidosimpledemo.rpserver.dto.GetChallengeDto;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsRequest;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AdapterApi {
    private final String COOKIE_NAME = "fido2-session-id";

    private final GetChallengeService getChallengeService;

    public AdapterApi(GetChallengeService getChallengeService) {
        this.getChallengeService = getChallengeService;
    }

    @PostMapping("/rp/attestation/options")
    public ServerPublicKeyCredentialCreationOptionsResponse getRegistrationChallenge(
            @RequestHeader String host,
            @RequestBody ServerPublicKeyCredentialCreationOptionsRequest request,
            HttpServletResponse httpServletResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        GetChallengeDto getChallengeDto = getChallengeService.getChallenge(host, request);
        httpServletResponse.addCookie(new Cookie(COOKIE_NAME, getChallengeDto.getSessionId()));
        return getChallengeDto.getResponse();
    }
}
