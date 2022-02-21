package com.example.fidosimpledemo.fidoserver.domain;

public interface SessionRepository {
    void save(Session session);
    Session getSession(String sessionId);
}
