package com.sparta.bluemoon.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sparta.bluemoon.config.GoogleConfigUtils;
import com.sparta.bluemoon.point.Point;
import com.sparta.bluemoon.security.jwt.JwtDecoder;
import com.sparta.bluemoon.user.requestDto.GoogleLoginRequest;
import com.sparta.bluemoon.user.responseDto.GoogleLoginResponse;
import com.sparta.bluemoon.user.responseDto.SocialLoginResponseDto;
import com.sparta.bluemoon.point.PointRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final GoogleConfigUtils configUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;
    private final RefreshRedisService refreshRedisService;
    private final JwtDecoder jwtDecoder;

    // token을 이용하여 사용자 정보 획득
    public ResponseEntity login(String jwtToken) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
            String email = getGoogleUserInfo(jwtToken);
            User googleUser = registerGoogleUserIfNeeded(email);
            return forceLogin(googleUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    private ResponseEntity forceLogin(User googleUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(googleUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Token 생성//리프레시 토큰도 생성
        final String token = JwtTokenUtils.generateAccessToken(userDetails);
        final String refreshToken = JwtTokenUtils.generaterefreshToken(userDetails);
        System.out.println("token = " + token);

        userRepository.save(googleUser);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        headers.set("RefreshToken","Bearer "+refreshToken);

        //redis에저장
        refreshRedisService.setValues(refreshToken,String.valueOf(googleUser.getId()));

        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(googleUser));
    }



    private User registerGoogleUserIfNeeded(String email) {
        String google = "google";
        User googleUser = userRepository.findByUsernameAndType(email, google).orElse(null);

        if (googleUser == null) {

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String type = "google";

            String nickname = "";
            googleUser = new User(email, encodedPassword, nickname, type);
            userRepository.save(googleUser);

            // 사용자 포인트 부여
            int mypoint = 0;
            int postCount = 1;
            int commentCount = 5;
            int lottoCount = 1;
            Point point = new Point(mypoint, googleUser, postCount, commentCount, lottoCount);
            pointRepository.save(point);
        }
        return googleUser;
    }

    private String getGoogleUserInfo(String jwtToken) throws JsonProcessingException {
        // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
        String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl() + "/tokeninfo").
            queryParam("id_token", jwtToken).toUriString();

        HttpHeaders headers = new HttpHeaders();
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            requestUrl,
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = jsonNode.get("email").asText();

        return email;
    }
}
