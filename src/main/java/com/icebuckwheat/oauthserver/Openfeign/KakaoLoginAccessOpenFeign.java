package com.icebuckwheat.oauthserver.Openfeign;

import com.icebuckwheat.oauthserver.Dto.KakaoAccessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-auth", url = "https://kauth.kakao.com")
public interface KakaoLoginAccessOpenFeign {

    @GetMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoAccessDto getAccessToken(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String client_id,
            @RequestParam(name = "redirect_uri") String redirect_uri,
            @RequestParam(name = "code") String code);

    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoAccessDto getRefreshAccessToken(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String client_id,
            @RequestParam(name = "refresh_token") String refresh_token
    );
}
