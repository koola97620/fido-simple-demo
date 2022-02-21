package com.example.fidosimpledemo.fidoserver.util;

import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

public class SignatureUtil {
    // RSA PSS
    public static boolean verifySHA256withRSAPssSignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA/PSS");
        MGF1ParameterSpec mgf1spec = MGF1ParameterSpec.SHA256;
        signature.setParameter(new PSSParameterSpec(mgf1spec.getDigestAlgorithm(), "MGF1",
                mgf1spec, 32, 1));

        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA384withRSAPssSignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA384withRSA/PSS");
        MGF1ParameterSpec mgf1spec = MGF1ParameterSpec.SHA384;
        signature.setParameter(new PSSParameterSpec(mgf1spec.getDigestAlgorithm(), "MGF1",
                mgf1spec, 48, 1));

        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA512withRSAPssSignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA512withRSA/PSS");
        MGF1ParameterSpec mgf1spec = MGF1ParameterSpec.SHA512;
        signature.setParameter(new PSSParameterSpec(mgf1spec.getDigestAlgorithm(), "MGF1",
                mgf1spec, 64, 1));

        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    // RSA PKCS v1.5
    public static boolean verifySHA1withRSASignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA256withRSASignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA384withRSASignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA384withRSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA512withRSASignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA256withECDSA(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA384withECDSA(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA384withECDSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifySHA512withECDSA(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA512withECDSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

    public static boolean verifyPureEdDSA(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("NONEwithEdDSA");
        signature.initVerify(publicKey);
        signature.update(messageBytes);

        return signature.verify(signatureBytes);
    }

}
