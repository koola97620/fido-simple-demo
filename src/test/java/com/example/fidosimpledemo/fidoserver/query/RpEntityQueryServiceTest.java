package com.example.fidosimpledemo.fidoserver.query;

import com.example.fidosimpledemo.AcceptanceTest;
import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import com.example.fidosimpledemo.rpserver.app.RpHelper;
import com.example.fidosimpledemo.rpserver.exception.NotFoundRpHostNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class RpEntityQueryServiceTest extends AcceptanceTest {
    @Autowired
    private RpQueryService rpQueryService;
    @Autowired
    private RpHelper helper;

    @DisplayName("성공")
    @Test
    void success() {
        helper.save(
                RpEntity.builder()
                        .id("localhost")
                        .name("test")
                        .description("test")
                        .build()
        );

        RpEntity rpEntity = rpQueryService.getRpEntity("localhost:8080");

        assertThat(rpEntity.getId()).isEqualTo("localhost");
        assertThat(rpEntity.getDescription()).isEqualTo("test");
    }

    @DisplayName("rp가 등록되어 있지 않을 때 예외 발생")
    @Test
    void notPresent() {
        helper.save(RpEntity.builder().id("localhost").name("test").description("test").build());

        assertThatThrownBy(() -> rpQueryService.getRpEntity("github.com:8080"))
                .isInstanceOf(NotFoundRpHostNameException.class);
    }


}
