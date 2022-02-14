package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.common.crypto.COSEAlgorithmIdentifier;
import lombok.Data;
import lombok.Getter;

@Data
public class PublicKeyCredentialParameters {
    private PublicKeyCredentialType type;
    private COSEAlgorithmIdentifier alg;
}
