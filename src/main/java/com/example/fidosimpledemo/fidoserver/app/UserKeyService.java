package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.*;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2CryptoException;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserKeyService {

    private final UserKeyRepository userKeyRepository;

    public UserKeyService(UserKeyRepository userKeyRepository) {
        this.userKeyRepository = userKeyRepository;
    }

    public List<UserKey> getUserKeyByRpIdAndUserId(String rpId, String userId) {
        List<UserKey> userKeys = new ArrayList<>();
        userKeyRepository.findAllByRpEntityIdAndUserId(rpId, userId)
                .forEach(userKeyEntity -> userKeys.add(convert(userKeyEntity)));
        return null;
    }

    private UserKey convert(UserKeyEntity userKeyEntity) {
        byte[] encodedPublicKey = Base64.getUrlDecoder().decode(userKeyEntity.getPublicKey());
        COSEAlgorithm algorithm = COSEAlgorithm.fromValue(userKeyEntity.getSignatureAlgorithm());
        PublicKey publicKey;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory;
        try {
            if (algorithm.isRSAAlgorithm()) {
                keyFactory = KeyFactory.getInstance("RSA");
            } else if (algorithm.isEdDSAAlgorithm()) {
                keyFactory = KeyFactory.getInstance("EdDSA");
            } else {
                keyFactory = KeyFactory.getInstance("ECDSA");
            }
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new FIDO2CryptoException("UserKey Convert Exception: " + e);

        }

        List<AuthenticatorTransport> authenticatorTransports = null;
        if (userKeyEntity.getTransports() != null && !userKeyEntity.getTransports().isEmpty()) {
            authenticatorTransports = new ArrayList<>();
            for (AuthenticatorTransportEntity authenticatorTransportEntity : userKeyEntity.getTransports()) {
                authenticatorTransports.add(AuthenticatorTransport.fromValue(authenticatorTransportEntity.getTransport()));
            }
        }

        UserKey.UserKeyBuilder builder = UserKey
                .builder()
                .aaguid(userKeyEntity.getAaguid())
                .algorithm(algorithm)
                .credentialId(userKeyEntity.getCredentialId())
                .userId(userKeyEntity.getUserId())
                .userName(userKeyEntity.getUsername())
                .publicKey(publicKey)
                .rpId(userKeyEntity.getRpEntity().getId())
                .signCounter(userKeyEntity.getSignCounter())
                .icon(userKeyEntity.getUserIcon())
                .transports(authenticatorTransports)
                .rk(userKeyEntity.getRk())
                .createdAt(userKeyEntity.getCreatedAt())
                .lastAuthenticatedAt(userKeyEntity.getLastAuthenticatedAt());

        return builder.build();
    }
}
