package com.example.fidosimpledemo.fidoserver.api;

import com.example.fidosimpledemo.AcceptanceTest;
import com.example.fidosimpledemo.rpserver.app.RpHelper;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.rpserver.domain.Rp;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;
import com.example.fidosimpledemo.rpserver.dto.RegOptionRequest;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("첼린지 생성")
class ChallengeApiTest extends  AcceptanceTest {
    @Autowired
    private RpHelper rpHelper;
    private Rp savedRp;
    private ServerPublicKeyCredentialUserEntity user;

    @BeforeEach
    public void setUp() {
        super.setUp();

        savedRp = rpHelper.save(Rp.builder().name("localhost").description("test").build());
        user = ServerPublicKeyCredentialUserEntity.of("testId");
    }

    @DisplayName("성공")
    @Test
    void success() {
        RegOptionRequest request = RegOptionRequest.builder()
                .rp(PublicKeyCredentialRpEntity.of(savedRp))
                .user(user)
                .build();

        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/fido2/req/challenge")
                .then().log().all()
                .extract();


        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        RegOptionResponse response = result.as(RegOptionResponse.class);
        assertThat(response.getSessionId()).isNotNull();
        assertThat(response.getChallenge()).isNotNull();
    }

    @Test
    void missRequest() {
        RegOptionRequest request = RegOptionRequest.builder()
                .rp(PublicKeyCredentialRpEntity.of(savedRp))
                .user(null)
                .build();

        ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/fido2/req/challenge")
                .then().log().all()
                .extract();


        assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        String error = result.jsonPath().get("error");
        assertThat(error).isEqualTo("Bad Request");
    }

}
