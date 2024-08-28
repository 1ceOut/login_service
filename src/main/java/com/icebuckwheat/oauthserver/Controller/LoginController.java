package com.icebuckwheat.oauthserver.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.icebuckwheat.oauthserver.Config.jwt;
import com.icebuckwheat.oauthserver.Dto.JwtResponse;
import com.icebuckwheat.oauthserver.Dto.UserEntityDto;
import com.icebuckwheat.oauthserver.Service.LoginService;
import com.icebuckwheat.oauthserver.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;
    private final com.icebuckwheat.oauthserver.Config.jwt jwt;
    private final UserService userService;

    public LoginController(LoginService loginService, jwt jwt, UserService userService) {
        this.loginService = loginService;
        this.jwt = jwt;
        this.userService = userService;
    }

    private static ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue())
                .maxAge(7 * 24 * 60 * 60)
                .build();
    }


    @GetMapping("/login/kakao")
    public ResponseEntity<Object> login(@RequestParam("accesstoken") String accessToken) throws JsonProcessingException {

        try {
            JwtResponse jwtResponse = loginService.KakaoLogin(accessToken, "kakao");
            ResponseCookie cookie = generateRefreshTokenCookie(jwtResponse.getRefreshToken());

            // 응답 헤더에 쿠키 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // ResponseEntity에 헤더와 본문 추가
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jwtResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception (log it, return an error response, etc.)
            return ResponseEntity.status(500).body(null); // Example error handling
        }
    }

    @GetMapping("/login/naver")
    public ResponseEntity<Object> Naverlogin(@RequestParam("accesstoken") String accessToken, @RequestParam("state") String state) throws JsonProcessingException {
        try {
            JwtResponse jwtResponse = loginService.NaverLogin(accessToken, state);
            ResponseCookie cookie = generateRefreshTokenCookie(jwtResponse.getRefreshToken());

            // 응답 헤더에 쿠키 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // ResponseEntity에 헤더와 본문 추가
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jwtResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception (log it, return an error response, etc.)
            return ResponseEntity.status(500).body(null); // Example error handling
        }
    }

    @GetMapping("/login/google")
    public ResponseEntity<Object> GoogleLogin(@RequestParam("accesstoken") String accessToken) throws JsonProcessingException {
        try {
            JwtResponse jwtResponse = loginService.GoogleLogin(accessToken);
            ResponseCookie cookie = generateRefreshTokenCookie(jwtResponse.getRefreshToken());

            // 응답 헤더에 쿠키 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

            // ResponseEntity에 헤더와 본문 추가
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(jwtResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception (log it, return an error response, etc.)
            return ResponseEntity.status(500).body(null); // Example error handling
        }
    }

    @GetMapping("/login/auto")
    public ResponseEntity<Object> checkAutoLogin(@CookieValue(value = "refreshToken",required = false)String refreshToken) throws JsonProcessingException {
        if (refreshToken==null || refreshToken.isEmpty() || refreshToken.isBlank()) return ResponseEntity.status(210).build();
        JwtResponse jwtResponse = loginService.checkRefreshToken(refreshToken);
        if (jwtResponse == null) {
            return ResponseEntity.status(500).body(null);
        }
        ResponseCookie cookie = generateRefreshTokenCookie(jwtResponse.getRefreshToken());

        // 응답 헤더에 쿠키 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(jwtResponse);
    }

    @DeleteMapping("/login/logout")
    public ResponseEntity<Object> logout(@CookieValue(value = "refreshToken",required = false)String refreshToken) throws JsonProcessingException {
        if (refreshToken==null || refreshToken.isEmpty()) return ResponseEntity.status(210).build();

        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(Cookie.SameSite.NONE.attributeValue())
                .maxAge(0)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(headers).body("");
    }

    @GetMapping("/login/getuser")
    public ResponseEntity<Object> getUserData(String user_id) {
        if (user_id==null || user_id.isEmpty()) return ResponseEntity.status(210).build();
        return ResponseEntity.ok(userService.getUser(user_id));
    }

    @GetMapping("/login/getalluser")
    public ResponseEntity<Object> getAllUserData() {
        List<UserEntityDto> userEntityDtos = userService.getAllUsers();
        if (userEntityDtos==null || userEntityDtos.isEmpty()) return ResponseEntity.status(210).build();
        return ResponseEntity.ok(userEntityDtos);
    }
}
