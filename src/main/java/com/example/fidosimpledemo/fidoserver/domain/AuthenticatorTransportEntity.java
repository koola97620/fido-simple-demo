package com.example.fidosimpledemo.fidoserver.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AUTHENTICATOR_TRANSPORT")
public class AuthenticatorTransportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false)
    private UserKeyEntity userKeyEntity;

    @Column
    @NotNull
    private String transport;

    protected AuthenticatorTransportEntity() {}

    public UserKeyEntity getUserKey() {
        return userKeyEntity;
    }

    public void setUserKey(UserKeyEntity userKeyEntity) {
        this.userKeyEntity = userKeyEntity;
    }

    public String getTransport() {
        return transport;
    }
}
