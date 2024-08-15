package com.icebuckwheat.oauthserver.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @Column(name = "user_id", nullable = false, length = 200)
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "photo", length = 100)
    private String photo;

    @Column(name = "nonpreference", length = 200)
    private String nonpreference;

    @Column(name = "preference", length = 200)
    private String preference;

    @Column(name = "allergy", length = 200)
    private String allergy;

    @Column(name = "illness", length = 200)
    private String illness;

    @Column(name = "height")
    private Byte height;

    @Column(name = "weight")
    private Byte weight;

    @Column(name = "role")
    private String role;

    @Column(name = "access_token", length = 512)
    private String accessToken;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

}