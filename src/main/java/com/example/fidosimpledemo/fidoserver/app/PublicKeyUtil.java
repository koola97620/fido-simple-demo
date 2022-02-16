package com.example.fidosimpledemo.fidoserver.app;

import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class PublicKeyUtil {
    public static PublicKey getRSAPublicKey(byte[] n, byte[] e)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigN = new BigInteger(1, n);
        BigInteger bigE = new BigInteger(1, e);
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(bigN, bigE);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(pubKeySpec);
    }

    public static PublicKey getECDSAPublicKey(byte[] x, byte[] y, String namedCurve) throws NoSuchAlgorithmException, InvalidKeySpecException {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec(namedCurve);
        ECNamedCurveSpec params = new ECNamedCurveSpec(namedCurve, spec.getCurve(), spec.getG(), spec.getN());

        // get EC point
        BigInteger xBig = new BigInteger(1, x);
        BigInteger yBig = new BigInteger(1, y);
        ECPoint point = new ECPoint(xBig, yBig);
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, params);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        //KeyFactory keyFactory = KeyFactory.getInstance("ECDSA");
        return keyFactory.generatePublic(pubKeySpec);
    }


//    public static PublicKey getEdDSAPublicKey(byte[] x, String namedCurve) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(namedCurve);
//
//        EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(x, spec);
//        KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");
//        return keyFactory.generatePublic(pubKeySpec);
//    }

    public static byte[] asUnsignedByteArray(
            BigInteger value) {
        byte[] bytes = value.toByteArray();

        if (bytes[0] == 0) {
            byte[] tmp = new byte[bytes.length - 1];

            System.arraycopy(bytes, 1, tmp, 0, tmp.length);

            return tmp;
        }

        return bytes;
    }

    public static PublicKey getEdDSAPublicKey(byte[] x, String namedCurve) throws NoSuchAlgorithmException, InvalidKeySpecException {
        EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(namedCurve);

        EdDSAPublicKeySpec pubKeySpec = new EdDSAPublicKeySpec(x, spec);
        KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");
        return keyFactory.generatePublic(pubKeySpec);
    }
}
