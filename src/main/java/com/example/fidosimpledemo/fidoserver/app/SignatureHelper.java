package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.COSEAlgorithm;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2InvalidAttestationException;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class SignatureHelper {
    public static boolean verifySignature(PublicKey publicKey, byte[] messageBytes, byte[] signatureBytes, COSEAlgorithm algorithm) {
        try {
            if (algorithm == COSEAlgorithm.ES256 ||
                    algorithm == COSEAlgorithm.ES256K) {
                return SignatureUtil.verifySHA256withECDSA(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.ES384) {
                return SignatureUtil.verifySHA384withECDSA(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.ES512) {
                return SignatureUtil.verifySHA512withECDSA(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.PS256) {
                return SignatureUtil.verifySHA256withRSAPssSignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.PS384) {
                return SignatureUtil.verifySHA384withRSAPssSignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.PS512) {
                return SignatureUtil.verifySHA512withRSAPssSignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.RS1) {
                return SignatureUtil.verifySHA1withRSASignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.RS256) {
                return SignatureUtil.verifySHA256withRSASignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.RS384) {
                return SignatureUtil.verifySHA384withRSASignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.RS512) {
                return SignatureUtil.verifySHA512withRSASignature(publicKey, messageBytes, signatureBytes);
            } else if (algorithm == COSEAlgorithm.EDDSA) {
                return SignatureUtil.verifyPureEdDSA(publicKey, messageBytes, signatureBytes);
            }
        } catch (GeneralSecurityException e) {
            throw new FIDO2InvalidAttestationException("SIGNATURE_VERIFICATION_ERROR");
        }

        return false;
    }
}
