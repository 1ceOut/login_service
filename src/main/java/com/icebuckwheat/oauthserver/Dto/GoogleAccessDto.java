package com.icebuckwheat.oauthserver.Dto;

import lombok.Data;

@Data
public class GoogleAccessDto {
    private String access_token;
    private String refresh_token;
    private long expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
