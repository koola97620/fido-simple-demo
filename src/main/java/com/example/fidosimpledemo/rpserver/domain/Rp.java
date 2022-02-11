package com.example.fidosimpledemo.rpserver.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "RP")
public class Rp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    private String description;

    protected Rp() {}

    @Builder
    public Rp(String name, String description) {
        this.name = name;
        this.description = description;
    }



}
