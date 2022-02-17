package com.example.fidosimpledemo.rpserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class VerifyCredentialResult {
    private ServerResponse serverResponse;
    private String aaguid;
    private String userId;
    private boolean userVerified;
    private boolean userPresent;
}
