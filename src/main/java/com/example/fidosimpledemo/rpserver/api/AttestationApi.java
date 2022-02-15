package com.example.fidosimpledemo.rpserver.api;

import com.example.fidosimpledemo.rpserver.app.AttestationService;
import com.example.fidosimpledemo.rpserver.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AttestationApi {
    private final String COOKIE_NAME = "fido2-session-id";

    private final AttestationService attestationService;

    public AttestationApi(AttestationService attestationService) {
        this.attestationService = attestationService;
    }

    @PostMapping("/rp/attestation/options")
    public ServerPublicKeyCredentialCreationOptionsResponse getRegistrationChallenge(
            @RequestHeader String host,
            @RequestBody ServerPublicKeyCredentialCreationOptionsRequest request,
            HttpServletResponse httpServletResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        GetChallengeDto getChallengeDto = attestationService.getChallenge(host, request);
        httpServletResponse.addCookie(new Cookie(COOKIE_NAME, getChallengeDto.getSessionId()));
        return getChallengeDto.getResponse();
    }

    @PostMapping("/rp/attestation/result")
    public AdapterServerResponse sendRegistrationResponse(
            @RequestHeader String host,
            @RequestBody AdapterRegServerPublicKeyCredential clientResponse, HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            return AdapterServerResponse.failed();
        }

        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        StringBuilder builder = new StringBuilder()
                .append(httpServletRequest.getScheme())
                .append("://")
                .append(host);

        HttpHeaders httpHeaders = new HttpHeaders();
        return attestationService.sendRegistration(host, sessionId, builder.toString(), clientResponse);
    }
}
