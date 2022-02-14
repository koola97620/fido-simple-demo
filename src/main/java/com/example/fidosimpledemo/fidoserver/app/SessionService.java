package com.example.fidosimpledemo.fidoserver.app;

import com.example.fidosimpledemo.common.crypto.HmacUtil;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2CryptoException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSessionData() {
        Session session = new Session();
        String sessionId = UUID.randomUUID().toString();
        SecretKey hmacKey;
        try {
            hmacKey = HmacUtil.generateHmacKey();
        } catch (NoSuchAlgorithmException e) {
            throw new FIDO2CryptoException("Exception during generating hmac key: " + e);
        }
        String hmacKeyString = Base64.getUrlEncoder().withoutPadding().encodeToString(hmacKey.getEncoded());
        session.setId(sessionId);
        session.setHmacKey(hmacKeyString);
        return session;
    }

    public void save(Session session) {
        sessionRepository.save(session);
    }
}
