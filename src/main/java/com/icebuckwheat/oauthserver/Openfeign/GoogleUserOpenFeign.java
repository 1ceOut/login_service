package com.icebuckwheat.oauthserver.Openfeign;

import com.icebuckwheat.oauthserver.Dto.GoogleUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-user", url = "https://www.googleapis.com")
public interface GoogleUserOpenFeign {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleUserDto getUser(@RequestParam("access_token")String accessToken);
}
