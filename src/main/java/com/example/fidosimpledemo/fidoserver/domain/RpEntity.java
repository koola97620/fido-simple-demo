package com.example.fidosimpledemo.fidoserver.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "RP")
public class RpEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String icon;
    @Column
    private String description;

}
