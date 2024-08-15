package com.icebuckwheat.oauthserver.Openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-user", url = "https://kapi.kakao.com")
public interface KakaoUesrOpenFeign {

    @GetMapping("/v2/user/me")
    String getUser(@RequestHeader("Authorization") String accessToken,@RequestHeader("Content-type") String contentType);
}
