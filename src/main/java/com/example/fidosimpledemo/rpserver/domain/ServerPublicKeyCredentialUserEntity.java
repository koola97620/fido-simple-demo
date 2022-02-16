package com.example.fidosimpledemo.rpserver.domain;


import com.example.fidosimpledemo.common.crypto.Digests;
import com.example.fidosimpledemo.rpserver.dto.ServerPublicKeyCredentialCreationOptionsRequest;
import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
public class ServerPublicKeyCredentialUserEntity extends PublicKeyCredentialEntity {
    @NotNull
    @Length(min = 1, max = 64)
    private String id;  //base64url encoded
    private String displayName;

    protected ServerPublicKeyCredentialUserEntity() {}


    private ServerPublicKeyCredentialUserEntity(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public static ServerPublicKeyCredentialUserEntity of(String id, String displayName) {
        return new ServerPublicKeyCredentialUserEntity(id, displayName);
    }

    public static ServerPublicKeyCredentialUserEntity of(String loginId) {
        return new ServerPublicKeyCredentialUserEntity(loginId, "");
    }

}
