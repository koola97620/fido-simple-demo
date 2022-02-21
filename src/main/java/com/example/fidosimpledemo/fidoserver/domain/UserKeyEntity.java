package com.example.fidosimpledemo.fidoserver.domain;


import com.example.fidosimpledemo.common.model.AttestationType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USER_KEY")
public class UserKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RpEntity rpEntity;

    @Column(nullable = false, length = 128)
    private String userId;

    @Column(length = 64)
    private String username;

    @Column(length = 128)
    private String userIcon;

    @Column(nullable = false, length = 36)
    private String aaguid;

    @Column(nullable = false, length = 256)
    private String credentialId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String publicKey;

    @Column(nullable = false)
    private int signatureAlgorithm;

    @Column
    private Long signCounter;

    @Column
    private AttestationType attestationType;

    @OneToMany(mappedBy = "userKeyEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AuthenticatorTransportEntity> transports = new ArrayList<>();

    @Column
    private Boolean rk;


    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastAuthenticatedAt;

}
