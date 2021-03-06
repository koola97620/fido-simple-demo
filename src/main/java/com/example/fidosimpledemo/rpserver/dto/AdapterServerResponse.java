package com.example.fidosimpledemo.rpserver.dto;

import com.example.fidosimpledemo.rpserver.domain.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdapterServerResponse {
    private Status status;
    private String errorMessage;
    private String sessionId;

    public static AdapterServerResponse failed() {
        return new AdapterServerResponse(Status.FAILED, "Cookie not found", null);
    }
}
