package com.icebuckwheat.oauthserver.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "간단한 사용자 정보")
public class User {
    @Schema(description = "사용자의 고유 ID")
    private String user_id;
    @Schema(description = "사용자의 이름")
    private String name;
    @Schema(description = "사용자의 이메일")
    private String email;
}
