package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.rpserver.dto.AuthOptionResponse;
import com.example.fidosimpledemo.rpserver.dto.RegOptionResponse;
import lombok.Getter;

import javax.persistence.Id;

@Getter
public class Session {
    @Id
    private String id;
    private String hmacKey;
    private RegOptionResponse regOptionResponse;
    private AuthOptionResponse authOptionResponse;
    private boolean served;

    public void setId(String id) {
        this.id = id;
    }

    public void setHmacKey(String hmacKey) {
        this.hmacKey = hmacKey;
    }

    public void setRegOptionResponse(RegOptionResponse response) {
        this.regOptionResponse = response;
    }
}
