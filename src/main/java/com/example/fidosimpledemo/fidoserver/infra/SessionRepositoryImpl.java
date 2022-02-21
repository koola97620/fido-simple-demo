package com.example.fidosimpledemo.fidoserver.infra;

import com.example.fidosimpledemo.fidoserver.domain.Session;
import com.example.fidosimpledemo.fidoserver.domain.SessionRepository;
import com.example.fidosimpledemo.fidoserver.exception.FIDO2SessionNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class SessionRepositoryImpl implements SessionRepository {
    private static final String KEY = "FIDO2::Session";

    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations valueOperations;

    @Value("${fido2.session-ttl-millis}")
    private long sessionTtlMillis;

    public SessionRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void save(Session session) {
        valueOperations.set(makeKey(session.getId()), session, sessionTtlMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public Session getSession(String sessionId) {
        return Optional.of((Session) valueOperations.get(makeKey(sessionId)))
                .orElseThrow( () -> new FIDO2SessionNotFoundException("Not found sessionId: " + sessionId));
    }

    private static String makeKey(String id) {
        return KEY + ":" + id;
    }
}
