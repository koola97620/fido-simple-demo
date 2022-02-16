package com.example.fidosimpledemo.fidoserver.app;

import java.util.Optional;

public interface SessionRepository {
    void save(Session session);
    Session getSession(String sessionId);
}
