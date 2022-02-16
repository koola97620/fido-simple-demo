package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.fidoserver.app.RegistrationService;
import com.example.fidosimpledemo.rpserver.api.RegisterCredential;
import com.example.fidosimpledemo.rpserver.dto.RegisterCredentialResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ResponseApi {

    private final RegistrationService registrationService;

    public ResponseApi(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/fido2/req/response")
    public RegisterCredentialResult sendRegResponse(@Valid @RequestBody RegisterCredential registerCredential) {
        return registrationService.regist(registerCredential.getServerPublicKeyCredential(), registerCredential.getSessionId(),
                registerCredential.getOrigin(), registerCredential.getRpId(), registerCredential.getTokenBinding());
    }
}
