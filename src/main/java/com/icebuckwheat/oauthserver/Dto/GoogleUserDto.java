package com.icebuckwheat.oauthserver.Dto;

import lombok.Data;

@Data
public class GoogleUserDto {
    private String id;
    private String email;
    private String verified_email;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
}
