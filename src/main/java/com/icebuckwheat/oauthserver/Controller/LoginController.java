package com.icebuckwheat.oauthserver.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.icebuckwheat.oauthserver.Config.jwt;
import com.icebuckwheat.oauthserver.Dto.AddinfoRequsetDto;
import com.icebuckwheat.oauthserver.Dto.GetUserDataResponseDto;
import com.icebuckwheat.oauthserver.Dto.JwtResponse;
import com.icebuckwheat.oauthserver.Dto.UserEntityDto;
import com.icebuckwheat.oauthserver.Service.LoginService;
import com.icebuckwheat.oauthserver.Service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Hidden
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

    @Hidden
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

    @Hidden
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

    @Hidden
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

    @Hidden
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

    @Tag(name = "로그인 사용자 API", description = "로그인한 사용자에 대한 간단한 정보를 가져오는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(
                    schema = @Schema(implementation = GetUserDataResponseDto.class)
            )),
            @ApiResponse(responseCode = "401", description = "제공해주신 user_id와 일치하는 값이 없거나 잘못된 user_id입니다.")
    })
    @GetMapping("/login/getuser")
    public ResponseEntity<Object> getUserData(@Parameter(name = "user_id",description = "사용자의 고유 ID값") String user_id) {
        if (user_id==null || user_id.isEmpty()) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(userService.getUser(user_id));
    }

    @Tag(name = "로그인 사용자들 API", description = "모든 사용자에 대한 모든 정보를 가져오는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = UserEntityDto.class))
            )),
            @ApiResponse(responseCode = "401", description = "현재 유저가 없습니다.")
    })
    @GetMapping("/login/getalluser")
    public ResponseEntity<Object> getAllUserData() {
        List<UserEntityDto> userEntityDtos = userService.getAllUsers();
        if (userEntityDtos==null || userEntityDtos.isEmpty()) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(userEntityDtos);
    }

    @PostMapping("/login/addinfo")
    public ResponseEntity<Object> addUserInfo(@RequestBody AddinfoRequsetDto requsetDto) {
        System.out.println(requsetDto);
        if (userService.addInfo(requsetDto)){
            return ResponseEntity.status(200).body(null);
        }
        return ResponseEntity.status(503).body(null);
    }

    @GetMapping("/login/broadcast")
    public ResponseEntity<Object> start(String userid) {
        userService.broadcastStart(userid);
        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/login/broadcast")
    public ResponseEntity<Object> stop(String userid) {
        userService.broadcastEnd(userid);
        return ResponseEntity.status(200).body(null);
    }
}
