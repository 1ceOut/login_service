package com.icebuckwheat.oauthserver.Openfeign;

import com.icebuckwheat.oauthserver.Dto.NaverAccessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naver-auth", url = "https://nid.naver.com/oauth2.0")
public interface NaverLoginAccessOpenFeign {

    @GetMapping("/token")
    NaverAccessDto getAccessToken(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "state") String state
    );

    @GetMapping("/token")
    NaverAccessDto getRefreshAccessToken(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret,
            @RequestParam(name = "refresh_token") String refreshToken
    );
}
