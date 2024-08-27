package com.icebuckwheat.oauthserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetUserDataResponseDto {
    private String name;
    private String photo;
}
