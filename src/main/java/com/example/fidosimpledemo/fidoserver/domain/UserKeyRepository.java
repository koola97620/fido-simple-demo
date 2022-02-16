package com.example.fidosimpledemo.fidoserver.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserKeyRepository extends JpaRepository<UserKeyEntity, Long> {
    List<UserKeyEntity> findAllByRpEntityIdAndUserId(String rpName, String userId);
    Optional<UserKeyEntity> findByRpEntityIdAndCredentialId(String id, String credentialId);
}
