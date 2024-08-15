package com.icebuckwheat.oauthserver.Openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naver-user", url = "https://openapi.naver.com")
public interface NaverUserOpenFeign {

    @GetMapping("/v1/nid/me")
    String getUser(
            @RequestHeader("Authorization") String accessToken
    );
}
