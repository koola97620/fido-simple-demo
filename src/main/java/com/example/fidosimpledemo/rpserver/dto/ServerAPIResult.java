package com.example.fidosimpledemo.rpserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface ServerAPIResult {
    ServerResponse getServerResponse();

    void setServerResponse(ServerResponse serverResponse);

    @JsonIgnore
    default boolean isSuccess() {
        return getServerResponse().isSuccess();
    }

    @JsonIgnore
    default boolean hasError() {
        return getServerResponse().hasError();
    }
}
