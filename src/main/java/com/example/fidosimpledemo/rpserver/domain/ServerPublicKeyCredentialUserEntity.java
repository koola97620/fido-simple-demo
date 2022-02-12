package com.example.fidosimpledemo.rpserver.domain;


import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsRequest;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@NoArgsConstructor
public class ServerPublicKeyCredentialUserEntity {
    @NotNull
    @Length(min = 1, max = 64)
    private String id;  //base64url encoded
    private String name;
    private String icon;

    private ServerPublicKeyCredentialUserEntity(String loginId) {
        this.id = createUserId(loginId);
        this.name = loginId;
    }

    public static ServerPublicKeyCredentialUserEntity of(ServerPublicKeyCredentialCreationOptionsRequest request) {
        return new ServerPublicKeyCredentialUserEntity(request.getLoginId());
    }

    public static ServerPublicKeyCredentialUserEntity of(String loginId) {
        return new ServerPublicKeyCredentialUserEntity(loginId);
    }


    private String createUserId(String username) {
        if (ObjectUtils.isEmpty(username)) {
            return null;
        }

        byte[] digest = Digests.sha256(username.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
