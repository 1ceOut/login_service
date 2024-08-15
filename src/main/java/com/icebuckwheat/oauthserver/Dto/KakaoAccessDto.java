package com.icebuckwheat.oauthserver.Dto;

import lombok.Data;

@Data
public class KakaoAccessDto {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String scope;
}
