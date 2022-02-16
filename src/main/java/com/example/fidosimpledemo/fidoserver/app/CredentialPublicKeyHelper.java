package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.*;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2PublicKeyException;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class CredentialPublicKeyHelper {
    public static PublicKey convert(CredentialPublicKey credentialPublicKey) {
        PublicKey publicKey;
        if (credentialPublicKey instanceof RSAKey) {
            RSAKey rsaKey = (RSAKey) credentialPublicKey;
            // convert
            try {
                publicKey = PublicKeyUtil.getRSAPublicKey(rsaKey.getN(), rsaKey.getE());
            } catch (GeneralSecurityException e) {
                throw new FIDO2PublicKeyException("USER_PUBLIC_KEY_INVALID_KEY_SPEC: " + e.getMessage());
            }
        } else if (credentialPublicKey instanceof ECCKey) {
            ECCKey eccKey = (ECCKey) credentialPublicKey;
            // convert
            try {
                publicKey = PublicKeyUtil.getECDSAPublicKey(eccKey.getX(), eccKey.getY(), eccKey.getCurve().getNamedCurve());
            } catch (GeneralSecurityException e) {
                throw new FIDO2PublicKeyException("USER_PUBLIC_KEY_INVALID_KEY_SPEC: " + e.getMessage());
            }

        } else if (credentialPublicKey instanceof OctetKey) {
            OctetKey octetKey = (OctetKey) credentialPublicKey;
            // convert
            try {
                publicKey = PublicKeyUtil.getEdDSAPublicKey(octetKey.getX(), octetKey.getCurve().getNamedCurve());
            } catch (GeneralSecurityException e) {
                throw new FIDO2PublicKeyException("USER_PUBLIC_KEY_INVALID_KEY_SPEC: " + e.getMessage());
            }
        } else {
            throw new FIDO2PublicKeyException("INVALID_CREDENTIAL_INSTANCE");
        }
        return publicKey;
    }

    public static COSEAlgorithm getCOSEAlgorithm(CredentialPublicKey credentialPublicKey) {
        COSEAlgorithm algorithm;
        if (credentialPublicKey instanceof RSAKey) {
            algorithm = ((RSAKey) credentialPublicKey).getAlgorithm();
        } else if (credentialPublicKey instanceof OctetKey) {
            algorithm = ((OctetKey) credentialPublicKey).getAlgorithm();
        } else {
            algorithm = ((ECCKey) credentialPublicKey).getAlgorithm();
        }
        return algorithm;
    }

}
