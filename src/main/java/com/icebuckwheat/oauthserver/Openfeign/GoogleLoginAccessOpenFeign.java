package com.icebuckwheat.oauthserver.Openfeign;

import com.icebuckwheat.oauthserver.Dto.GoogleAccessDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-auth", url = "https://oauth2.googleapis.com")
public interface GoogleLoginAccessOpenFeign {

    @PostMapping("/token")
    GoogleAccessDto getAccessToken(
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirect_uri,
            @RequestParam("grant_type") String grantType
    );

}
