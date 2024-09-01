package com.icebuckwheat.oauthserver.Entity;

import com.icebuckwheat.oauthserver.Dto.UserEntityDto;
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

    @Column(name = "photo", length = 500)
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
    private int height;

    @Column(name = "weight")
    private int weight;

    @Column(name = "role")
    private String role;

    @Column(name = "access_token", length = 512)
    private String accessToken;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;


    public UserEntityDto toDto() {
        return UserEntityDto.builder()
                .userId(userId)
                .email(email)
                .name(name)
                .photo(photo)
                .nonpreference(nonpreference)
                .preference(preference)
                .allergy(allergy)
                .illness(illness)
                .height(height)
                .weight(weight)
                .role(role)
                .build();
    }
}