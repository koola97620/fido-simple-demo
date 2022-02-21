package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.AcceptanceTest;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.rpserver.app.RpHelper;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.dto.AdapterRegServerPublicKeyCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("인증정보 저장")
class ResponseApiTest extends AcceptanceTest {
    @Autowired
    private RpHelper rpHelper;
    private RpEntity savedRpEntity;
    private ServerPublicKeyCredentialUserEntity user;

    @BeforeEach
    public void setUp() {
        super.setUp();

        savedRpEntity = rpHelper.save(RpEntity.builder().id("localhost").name("test").description("test").build());
        user = ServerPublicKeyCredentialUserEntity.of("testId");
    }

    @Test
    void success() {

        AdapterRegServerPublicKeyCredential clientResponse = new AdapterRegServerPublicKeyCredential();






    }

}
