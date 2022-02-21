package com.example.fidosimpledemo.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse {
    private String description;
    private String internalError;
    private Integer internalErrorCode;
    private String internalErrorCodeDescription;

    @JsonIgnore
    public boolean isSuccess() {
        return !hasError();
    }

    @JsonIgnore
    public boolean hasError() {
        return internalErrorCode != null && internalErrorCode != 0;
    }
}
