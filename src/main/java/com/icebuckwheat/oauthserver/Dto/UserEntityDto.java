package com.icebuckwheat.oauthserver.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "사용자의 고유 ID")
    @NotNull
    @NotEmpty
    String userId;
    @Schema(description = "사용자의 이메일")
    @NotNull
    @Email
    @NotEmpty
    String email;
    @Schema(description = "사용자의 이름")
    @NotNull
    @NotEmpty
    String name;
    @Schema(description = "사용자의 이미지")
    @NotNull
    @NotEmpty
    String photo;

    @Schema(description = "사용자의 비선호 음식")
    String nonpreference;
    @Schema(description = "사용자의 선호 음식")
    String preference;
    @Schema(description = "사용자의 알러지")
    String allergy;
    @Schema(description = "사용자의 지병")
    String illness;
    @Schema(description = "사용자의 키")
    Byte height;
    @Schema(description = "사용자의 몸무게")
    Byte weight;
    @Schema(description = "사용자의 권한")
    @NotNull
    String role;
}