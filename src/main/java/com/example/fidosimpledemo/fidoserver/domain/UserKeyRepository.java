package com.example.fidosimpledemo.fidoserver.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserKeyRepository extends JpaRepository<UserKeyEntity, Long> {
    List<UserKeyEntity> findAllByRpEntityIdAndUserId(String rpName, String userId);
}
