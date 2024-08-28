package com.icebuckwheat.oauthserver.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.icebuckwheat.oauthserver.Entity.UserEntity}
 */
@Value
@Builder
public class UserEntityDto implements Serializable {
    @NotNull
    @NotEmpty
    String userId;
    @NotNull
    @Email
    @NotEmpty
    String email;
    @NotNull
    @NotEmpty
    String name;
    @NotNull
    @NotEmpty
    String photo;
    String nonpreference;
    String preference;
    String allergy;
    String illness;
    Byte height;
    Byte weight;
    @NotNull
    String role;
}