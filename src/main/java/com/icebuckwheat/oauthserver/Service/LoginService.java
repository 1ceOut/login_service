package com.icebuckwheat.oauthserver.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icebuckwheat.oauthserver.Config.jwt;
import com.icebuckwheat.oauthserver.Dto.*;
import com.icebuckwheat.oauthserver.Entity.UserEntity;
import com.icebuckwheat.oauthserver.Openfeign.*;
import com.icebuckwheat.oauthserver.Repository.UserEntityRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    final KakaoLoginAccessOpenFeign kakaoLoginAccessOpenFeign;
    final KakaoUesrOpenFeign kakaoUesrOpenFeign;

    final NaverLoginAccessOpenFeign naverLoginAccessOpenFeign;
    final NaverUserOpenFeign naverUserOpenFeign;

    final GoogleLoginAccessOpenFeign googleLoginAccessOpenFeign;
    final GoogleUserOpenFeign googleUserOpenFeign;


    final UserEntityRepository userEntityRepository;
    final jwt jwt;

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${login_redirect_url}")
    private String RedirectUri;

    @Value("${naver.client_id}")
    private String naverClientId;

    @Value("${naver.client_secret}")
    private String naverClientSecret;

    @Value("${google.client_id}")
    private String googleClientId;

    @Value("${google.client_secret}")
    private String googleClientSecret;



    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Transactional
    public JwtResponse KakaoLogin(String code, String provider) throws FeignException, JsonProcessingException {

        UserEntity user = null;

        KakaoAccessDto kakaoAccessDto = kakaoGetAccessToken(code);
        System.out.println(kakaoAccessDto);
        JsonNode jsonNode = objectMapper.readTree(kakaoUesrOpenFeign.getUser("Bearer " + kakaoAccessDto.getAccess_token(),"application/x-www-form-urlencoded;charset=utf-8"));

        if (!userEntityRepository.existsById(provider+" "+jsonNode.get("id"))){
            user = new UserEntity();
            user.setUserId(provider+" "+jsonNode.get("id"));
            user.setName(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
            user.setEmail(jsonNode.get("kakao_account").get("email").asText());
            user.setPhoto(jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText());
            user.setRole("ROLE_NEED_INSERT");
            user.setAccessToken(kakaoAccessDto.getAccess_token());
            user.setRefreshToken(kakaoAccessDto.getRefresh_token());
            user.setBroadcast(false);
            userEntityRepository.save(user);
        }
        else {
            user = userEntityRepository.findById(provider+" "+jsonNode.get("id")).get();
            user.setName(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
            user.setEmail(jsonNode.get("kakao_account").get("email").asText());
            user.setPhoto(jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText());
            user.setAccessToken(kakaoAccessDto.getAccess_token());
            user.setRefreshToken(kakaoAccessDto.getRefresh_token());
            userEntityRepository.save(user);
        }

        System.out.println(user);
        userService.sendUserId(convert_User(user));
        userService.sendUserData(user.toDto());

        return JwtResponse.builder()
                .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                .build();
    }

    public KakaoAccessDto kakaoGetAccessToken(String code) {
        return kakaoLoginAccessOpenFeign.getAccessToken(
                "authorization_code",
                kakaoClientId,
                RedirectUri+"kakao",
                code);
    }

    @Transactional
    public JwtResponse NaverLogin(String accessToken, String state) throws JsonProcessingException {
        NaverAccessDto naverAccessDto = naverLoginAccessOpenFeign.getAccessToken(
                "authorization_code",
                naverClientId,
                naverClientSecret,
                accessToken,
                state
        );

        JsonNode jsonNode = objectMapper.readTree(naverUserOpenFeign.getUser("Bearer " + naverAccessDto.getAccess_token())).get("response");

        UserEntity user = null;
        String buf = "naver " + jsonNode.get("id").toString().replace("\"","");

        if (!userEntityRepository.existsById(buf)){
            System.out.println("new user");
            user = new UserEntity();
            user.setUserId(("naver "+jsonNode.get("id")).replace("\"",""));
            user.setName(jsonNode.get("name").asText());
            user.setEmail(jsonNode.get("email").asText());
            user.setPhoto(jsonNode.get("profile_image").asText());
            user.setRole("ROLE_NEED_INSERT");
            user.setAccessToken(naverAccessDto.getAccess_token());
            user.setRefreshToken(naverAccessDto.getRefresh_token());
            user.setBroadcast(false);
            userEntityRepository.save(user);
        }
        else {
            user = userEntityRepository.findById(buf).get();
            user.setUserId(("naver "+jsonNode.get("id")).replace("\"",""));
            user.setName(jsonNode.get("name").asText());
            user.setEmail(jsonNode.get("email").asText());
            user.setPhoto(jsonNode.get("profile_image").asText());
            user.setAccessToken(naverAccessDto.getAccess_token());
            user.setRefreshToken(naverAccessDto.getRefresh_token());
            userEntityRepository.save(user);
        }

        System.out.println(user);
        userService.sendUserId(convert_User(user));
        userService.sendUserData(user.toDto());

        return JwtResponse.builder()
                .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                .build();
    }

    @Transactional
    public JwtResponse GoogleLogin(String accessToken) throws JsonProcessingException {

        GoogleAccessDto googleAccessDto = googleLoginAccessOpenFeign.getAccessToken(
                accessToken,
                googleClientId,
                googleClientSecret,
                RedirectUri+"google",
                "authorization_code"
        );


        GoogleUserDto userDto = googleUserOpenFeign.getUser(googleAccessDto.getAccess_token());

        UserEntity user = null;

        if (!userEntityRepository.existsById("google "+userDto.getId())){
            user = new UserEntity();
            user.setUserId("google " + userDto.getId());
            user.setName(userDto.getFamily_name()+userDto.getGiven_name());
            user.setEmail(userDto.getEmail());
            user.setPhoto(userDto.getPicture());
            user.setRole("ROLE_NEED_INSERT");
            user.setAccessToken(googleAccessDto.getAccess_token());
            user.setRefreshToken(googleAccessDto.getRefresh_token());
            user.setBroadcast(false);
            userEntityRepository.save(user);
        }
        else {
            user = userEntityRepository.findById("google "+userDto.getId()).get();
            user.setName((userDto.getFamily_name()==null?"":userDto.getFamily_name())+userDto.getGiven_name());
            user.setEmail(userDto.getEmail());
            user.setPhoto(userDto.getPicture());
            user.setAccessToken(googleAccessDto.getAccess_token());
            user.setRefreshToken(googleAccessDto.getRefresh_token());
            userEntityRepository.save(user);
        }

        System.out.println(user);
        userService.sendUserId(convert_User(user));
        userService.sendUserData(user.toDto());

        return JwtResponse.builder()
                .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                .build();
    }

    public User convert_User(UserEntity userEntity) {
        return User.builder()
                .user_id(userEntity.getUserId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .photo(userEntity.getPhoto())
                .build();
    }

    @Transactional
    public JwtResponse checkRefreshToken(String refreshToken) throws JsonProcessingException {

        if (jwt.isJwtValid(refreshToken)){
            UserEntity user = userEntityRepository.findById(jwt.getUserId(refreshToken)).get();

            if (user.getUserId().split(" ")[0].equals("google")){
                GoogleAccessDto googleAccessDto = googleLoginAccessOpenFeign.getRefreshAccessToken(
                        googleClientId,
                        googleClientSecret,
                        user.getRefreshToken(),
                        "refresh_token"
                );
                GoogleUserDto userDto = googleUserOpenFeign.getUser(googleAccessDto.getAccess_token());
                user.setName((userDto.getFamily_name()==null?"":userDto.getFamily_name())+userDto.getGiven_name());
                user.setEmail(userDto.getEmail());
                user.setPhoto(userDto.getPicture());
                user.setAccessToken(googleAccessDto.getAccess_token());
                user.setRefreshToken(googleAccessDto.getRefresh_token());
                userEntityRepository.save(user);

                userService.sendUserId(convert_User(user));
                userService.sendUserData(user.toDto());

                return JwtResponse.builder()
                        .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                        .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                        .build();

            } else if (user.getUserId().split(" ")[0].equals("naver")) {
                NaverAccessDto naverAccessDto = naverLoginAccessOpenFeign.getRefreshAccessToken(
                        "refresh_token",
                        naverClientId,
                        naverClientSecret,
                        user.getRefreshToken()
                );

                JsonNode jsonNode = objectMapper.readTree(naverUserOpenFeign.getUser("Bearer " + naverAccessDto.getAccess_token())).get("response");

                user.setUserId(("naver "+jsonNode.get("id")).replace("\"",""));
                user.setName(jsonNode.get("name").asText());
                user.setEmail(jsonNode.get("email").asText());
                user.setPhoto(jsonNode.get("profile_image").asText());
                user.setAccessToken(naverAccessDto.getAccess_token());
                user.setRefreshToken(naverAccessDto.getRefresh_token());
                userEntityRepository.save(user);

                userService.sendUserId(convert_User(user));

                return JwtResponse.builder()
                        .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                        .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                        .build();
            }
            else {
                KakaoAccessDto kakaoAccessDto = kakaoLoginAccessOpenFeign.getRefreshAccessToken(
                        "refresh_token",
                        kakaoClientId,
                        user.getRefreshToken()
                );

                JsonNode jsonNode = objectMapper.readTree(kakaoUesrOpenFeign.getUser("Bearer " + kakaoAccessDto.getAccess_token(),"application/x-www-form-urlencoded;charset=utf-8"));

                user.setName(jsonNode.get("kakao_account").get("profile").get("nickname").asText());
                user.setEmail(jsonNode.get("kakao_account").get("email").asText());
                user.setPhoto(jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText());
                user.setAccessToken(kakaoAccessDto.getAccess_token());
                user.setRefreshToken(kakaoAccessDto.getRefresh_token());
                userEntityRepository.save(user);

                userService.sendUserId(convert_User(user));
                userService.sendUserData(user.toDto());

                return JwtResponse.builder()
                        .AccessToken(jwt.MakeAccessJwtToken(user.getUserId(),user.getRole(),user.getName(),user.getPhoto()))
                        .RefreshToken(jwt.MakeRefreshJwtToken(user.getUserId(),user.getRole(),user.getName()))
                        .build();
            }
        } else return null;
    }
}
