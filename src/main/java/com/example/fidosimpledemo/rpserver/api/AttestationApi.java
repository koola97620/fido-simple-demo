package com.example.fidosimpledemo.rpserver.api;

import com.example.fidosimpledemo.rpserver.app.RpAssertionService;
import com.example.fidosimpledemo.rpserver.app.RpAttestationService;
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

    private final RpAttestationService rpAttestationService;
    private final RpAssertionService rpAssertionService;

    public AttestationApi(RpAttestationService rpAttestationService, RpAssertionService rpAssertionService) {
        this.rpAttestationService = rpAttestationService;
        this.rpAssertionService = rpAssertionService;
    }

    @PostMapping("/rp/attestation/options")
    public ServerPublicKeyCredentialCreationOptionsResponse getRegistrationChallenge(
            @RequestHeader String host,
            @RequestBody ServerPublicKeyCredentialCreationOptionsRequest request,
            HttpServletResponse httpServletResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();
        GetChallengeDto getChallengeDto = rpAttestationService.getChallenge(host, request);
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
        return rpAttestationService.sendRegistration(host, sessionId, builder.toString(), clientResponse);
    }

    @PostMapping("/rp/assertion/options")
    public ServerPublicKeyCredentialGetOptionsResponse getAuthenticationChallenge(
            @RequestHeader String host,
            @RequestBody ServerPublicKeyCredentialGetOptionsRequest optionRequest,
            HttpServletResponse httpServletResponse) {
        HttpHeaders httpHeaders = new HttpHeaders();

        ServerPublicKeyCredentialGetOptionsResponseDto responseDto =
                rpAssertionService.sendAssertion(host, optionRequest);

        httpServletResponse.addCookie(new Cookie(COOKIE_NAME, responseDto.getSessionId()));
        return responseDto.getResponse();
    }
}
