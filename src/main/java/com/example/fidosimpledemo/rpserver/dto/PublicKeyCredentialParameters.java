package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.common.crypto.COSEAlgorithmIdentifier;
import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialType;
import lombok.Data;

@Data
public class PublicKeyCredentialParameters {
    private PublicKeyCredentialType type;
    private COSEAlgorithmIdentifier alg;
}
