package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.rpserver.domain.PublicKeyCredentialRpEntity;
import com.example.fidosimpledemo.rpserver.domain.ServerPublicKeyCredentialUserEntity;

import java.util.List;

public class RegOptionResponse {
    private PublicKeyCredentialRpEntity rp;
    private ServerPublicKeyCredentialUserEntity user;
    private String challenge;   // base64 url encoded
    private Long timeout;
    private String sessionId;
}
