package com.example.fidosimpledemo.fidoserver.infra;

import com.example.fidosimpledemo.fidoserver.domain.COSEAlgorithm;
import com.example.fidosimpledemo.fidoserver.domain.COSEEllipticCurve;
import com.example.fidosimpledemo.fidoserver.domain.COSEKeyType;
import com.example.fidosimpledemo.fidoserver.util.CredentialPublicKey;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;
import lombok.Builder;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
@Builder
public class OctetKey extends CredentialPublicKey {
    private COSEAlgorithm algorithm;
    private COSEEllipticCurve curve;
    private byte[] x;

    @Override
    public byte[] encode() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CBORFactory factory = new CBORFactory();
        CBORGenerator gen = factory.createGenerator(outputStream);

        // start map
        gen.writeStartObject();

        gen.writeFieldId(1);    // kty label
        gen.writeNumber(COSEKeyType.OKP.getValue()); // EC2 kty

        gen.writeFieldId(3);    // alg label
        gen.writeNumber(algorithm.getValue());  // alg value

        gen.writeFieldId(-1);   // crv label
        gen.writeNumber(curve.getValue());  // crv value

        gen.writeFieldId(-2);   // x label
        gen.writeBinary(x); // x value

        // end map
        gen.writeEndObject();

        gen.close();

        return outputStream.toByteArray();
    }
}
