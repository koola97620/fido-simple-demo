package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.AcceptanceTest;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.rpserver.app.RpHelper;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("첼린지 생성")
class ChallengeApiTest extends AcceptanceTest {
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

    @DisplayName("성공")
    @Test
    void success() {
        RegOptionRequest request = 요청_생성(savedRpEntity, user);

        ExtractableResponse<Response> result = 첼린지_생성_요청(request);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        RegOptionResponse response = result.as(RegOptionResponse.class);
        assertThat(response.getSessionId()).isNotNull();
        assertThat(response.getChallenge()).isNotNull();
    }

    @DisplayName("요청에 null 있을시 에러 발생")
    @Test
    void missRequest() {
        RegOptionRequest request = 요청_생성(savedRpEntity, null);

        ExtractableResponse<Response> result = 첼린지_생성_요청(request);

        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        String error = result.jsonPath().get("error");
        assertThat(error).isEqualTo("Bad Request");
    }

    private RegOptionRequest 요청_생성(RpEntity rpEntity, ServerPublicKeyCredentialUserEntity user) {
        PublicKeyCredentialRpEntity publicKeyCredentialRpEntity = new PublicKeyCredentialRpEntity();
        publicKeyCredentialRpEntity.setId(rpEntity.getId());
        return RegOptionRequest.builder()
                .rp(publicKeyCredentialRpEntity)
                .user(user)
                .build();
    }

    private ExtractableResponse<Response> 첼린지_생성_요청(RegOptionRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/fido2/req/challenge")
                .then().log().all()
                .extract();
    }

}
