package com.example.fidosimpledemo.rpserver.domain;

import com.example.fidosimpledemo.fidoserver.domain.RpEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class PublicKeyCredentialRpEntity extends PublicKeyCredentialEntity{
    private String id;
}
