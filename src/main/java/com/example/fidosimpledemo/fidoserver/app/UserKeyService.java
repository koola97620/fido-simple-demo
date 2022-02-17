package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.fidoserver.domain.*;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2CryptoException;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2RpNotFoundException;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2UserKeyNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserKeyService {

    private final UserKeyRepository userKeyRepository;
    private final RpRepository rpRepository;

    public UserKeyService(UserKeyRepository userKeyRepository, RpRepository rpRepository) {
        this.userKeyRepository = userKeyRepository;
        this.rpRepository = rpRepository;
    }

    public List<UserKey> getUserKeyByRpIdAndUserId(String rpId, String userId) {
        List<UserKey> userKeys = new ArrayList<>();
        userKeyRepository.findAllByRpEntityIdAndUserId(rpId, userId)
                .forEach(userKeyEntity -> userKeys.add(convert(userKeyEntity)));
        return userKeys;
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
                keyFactory = KeyFactory.getInstance("EC");
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
                .attestationType(userKeyEntity.getAttestationType())
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

    public boolean containsCredential(String rpId, String credentialId) {
        return userKeyRepository.findByRpEntityIdAndCredentialId(rpId, credentialId).isPresent();
    }

    public UserKey saveUser(UserKey user) {
        UserKeyEntity userKeyEntity = convert(user);

        if (user.getTransports() != null && !user.getTransports().isEmpty()) {
            for (AuthenticatorTransport authenticatorTransport : user.getTransports()) {
                AuthenticatorTransportEntity authenticatorTransportEntity = new AuthenticatorTransportEntity(authenticatorTransport.getValue());
                authenticatorTransportEntity.setUserKeyEntity(userKeyEntity);
                userKeyEntity.getTransports().add(authenticatorTransportEntity);
            }
        }

        userKeyEntity.setCreatedAt(LocalDateTime.now());
        userKeyRepository.save(userKeyEntity);
        return user;
    }

    private UserKeyEntity convert(UserKey userKey) {
        Optional<RpEntity> optionalRpEntity = rpRepository.findById(userKey.getRpId());
        RpEntity rpEntity;
        if (optionalRpEntity.isPresent()) {
            rpEntity = optionalRpEntity.get();
        } else {
            throw new FIDO2RpNotFoundException("Not Found RpId: " + userKey.getRpId());
        }

        UserKeyEntity.UserKeyEntityBuilder builder = UserKeyEntity
                .builder()
                .rpEntity(rpEntity)
                .publicKey(Base64.getUrlEncoder().withoutPadding().encodeToString(userKey.getPublicKey().getEncoded()))
                .userIcon(userKey.getIcon())
                .userId(userKey.getUserId())
                .aaguid(userKey.getAaguid())
                .attestationType(userKey.getAttestationType())
                .credentialId(userKey.getCredentialId())
                .signatureAlgorithm(userKey.getAlgorithm().getValue())
                .signCounter(userKey.getSignCounter())
                .username(userKey.getUserName())
                .transports(new ArrayList<>())
                .rk(userKey.getRk());

//        if (userKey.getCredProtect() == null) {
//            builder.credProtect(CredentialProtectionPolicy.USER_VERIFICATION_OPTIONAL.getValue());  // default
//        }

        return builder.build();
    }

    public UserKey getWithCredentialId(String rpId, String credentialId) {
        UserKeyEntity userKeyEntity = findByRpEntityIdAndCredentialId(rpId, credentialId);
        return convert(userKeyEntity);
    }

    private UserKeyEntity findByRpEntityIdAndCredentialId(String rpId, String credentialId) {
        return userKeyRepository
                .findByRpEntityIdAndCredentialId(rpId, credentialId)
                .orElseThrow( () -> new FIDO2UserKeyNotFoundException("rpId: " + rpId + ",credentialId: " + credentialId));
    }

    @Transactional
    public void update(UserKey user) {
        UserKeyEntity userKeyEntity = findByRpEntityIdAndCredentialId(user.getRpId(), user.getCredentialId());
        userKeyEntity.setSignCounter(user.getSignCounter());
        userKeyEntity.setLastAuthenticatedAt(LocalDateTime.now());
        userKeyRepository.save(userKeyEntity);
    }
}
