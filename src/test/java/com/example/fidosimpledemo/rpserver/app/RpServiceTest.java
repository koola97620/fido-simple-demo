package com.example.fidosimpledemo.rpserver.app;

import com.example.fidosimpledemo.IntegrationTest;
import com.example.fidosimpledemo.rpserver.domain.Rp;
import com.example.fidosimpledemo.rpserver.exception.NotFoundRpHostNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RpServiceTest extends IntegrationTest {
    @Autowired
    private RpService rpService;
    @Autowired
    private RpHelper helper;

    @DisplayName("성공")
    @Test
    void success() {
        helper.save(
                Rp.builder()
                        .name("localhost")
                        .description("test")
                        .build()
        );

        Rp rp = rpService.getRpEntity("localhost:8080");

        assertThat(rp.getName()).isEqualTo("localhost");
        assertThat(rp.getDescription()).isEqualTo("test");
    }

    @DisplayName("rp가 등록되어 있지 않을 때 예외 발생")
    @Test
    void notPresent() {
        helper.save(Rp.builder().name("localhost").description("test").build());

        assertThatThrownBy(() -> rpService.getRpEntity("github.com:8080"))
                .isInstanceOf(NotFoundRpHostNameException.class);
    }


}