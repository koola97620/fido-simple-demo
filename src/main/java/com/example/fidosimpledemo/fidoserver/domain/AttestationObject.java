package com.example.fidosimpledemo.fidoserver.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@JsonDeserialize(using = AttestationObjectDeserializer.class)
@Data
public class AttestationObject {
    private AuthenticatorData authData;
    private AttestationStatementFormatIdentifier fmt;
    private AttestationStatement attStmt;
}
