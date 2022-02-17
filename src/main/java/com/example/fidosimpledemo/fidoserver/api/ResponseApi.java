package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.fidoserver.app.ResponseService;
import com.example.fidosimpledemo.rpserver.api.RegisterCredential;
import com.example.fidosimpledemo.rpserver.dto.RegisterCredentialResult;
import com.example.fidosimpledemo.rpserver.dto.VerifyCredentialRequest;
import com.example.fidosimpledemo.rpserver.dto.VerifyCredentialResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ResponseApi {

    private final ResponseService responseService;

    public ResponseApi(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping("/fido2/req/response")
    public RegisterCredentialResult sendRegResponse(@Valid @RequestBody RegisterCredential registerCredential) {
        return responseService.regist(registerCredential.getServerPublicKeyCredential(), registerCredential.getSessionId(),
                registerCredential.getOrigin(), registerCredential.getRpId(), registerCredential.getTokenBinding());
    }

    @PostMapping("/fido2/auth/response")
    public VerifyCredentialResult sendAuthResponse(@Valid @RequestBody VerifyCredentialRequest verifyCredential) {
        return responseService.handleAssertion(verifyCredential.getServerPublicKeyCredential(), verifyCredential.getSessionId(),
                verifyCredential.getOrigin(), verifyCredential.getRpId(), verifyCredential.getTokenBinding());

    }
}
