package com.example.fidosimpledemo.rpserver.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RpRepository extends JpaRepository<Rp, Long> {
    Optional<Rp> findRpEntityByName(String name);
}
