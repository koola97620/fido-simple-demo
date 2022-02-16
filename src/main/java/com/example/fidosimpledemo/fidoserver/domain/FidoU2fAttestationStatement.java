package com.example.fidosimpledemo.fidoserver.domain;

import lombok.Data;

import java.util.List;

@Data
public class FidoU2fAttestationStatement extends AttestationStatement {
    private List<byte[]> x5c;
    private byte[] sig;
}
