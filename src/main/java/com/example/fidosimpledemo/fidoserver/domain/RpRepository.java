package com.example.fidosimpledemo.fidoserver.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RpRepository extends JpaRepository<RpEntity, String> {
    Optional<RpEntity> findRpEntityById(String id);
    boolean existsRpById(String id);
}
