package com.example.fidosimpledemo.rpserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicKeyCredentialRpEntity {
    private String name;
    private String icon;

    private PublicKeyCredentialRpEntity(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public static PublicKeyCredentialRpEntity of(Rp rpEntity) {
        return new PublicKeyCredentialRpEntity(rpEntity.getName(), rpEntity.getIcon());
    }
}
