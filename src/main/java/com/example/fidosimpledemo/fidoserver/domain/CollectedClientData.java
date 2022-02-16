package com.example.fidosimpledemo.fidoserver.domain;

import com.example.fidosimpledemo.rpserver.api.TokenBinding;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectedClientData {
    private String type;
    private String challenge;
    private String origin;
    private TokenBinding tokenBinding;
    private String androidPackageName;  // Android specific e.g., com.chrome.canary
}
