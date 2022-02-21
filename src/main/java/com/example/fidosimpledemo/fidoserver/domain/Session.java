package com.example.fidosimpledemo.fidoserver.domain;

import com.example.fidosimpledemo.common.model.AuthOptionResponse;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import lombok.Data;

import javax.persistence.Id;

@Data
public class Session {
    @Id
    private String id;
    private String hmacKey;
    private RegOptionResponse regOptionResponse;
    private AuthOptionResponse authOptionResponse;
    private boolean served;

}
