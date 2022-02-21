package com.example.fidosimpledemo.common.model;

import com.example.fidosimpledemo.rpserver.dto.Base64Encoded;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Credential {
    @NotBlank
    @Base64Encoded
    private String id;
    @NotNull
    private PublicKeyCredentialType type;
}
