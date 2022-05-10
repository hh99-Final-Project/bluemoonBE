package com.sparta.bluemoon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.config.KakaoConfigUtils;
import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.response.SocialLoginResponseDto;
import com.sparta.bluemoon.repository.PointRepository;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoConfigUtils configUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;

    public ResponseEntity<Object> requestAuthCodeFromKakao() {
        String authUrl = configUtils.kakaoInitUrl();
        System.out.println("authUrl = " + authUrl);
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

//    public ResponseEntity<KakaoLoginDto> login(String authCode) {
    public ResponseEntity login(String jwtToken) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
//            String jwtToken = getAccessToken(authCode);
            System.out.println("jwtToken = " + jwtToken);
            // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
            String nickname = getKakaoUserInfo(jwtToken);
            // 3. "카카오 사용자 정보"로 필요시 회원가입
            User kakaoUser = registerKakaoUserIfNeeded(nickname);
            // 4. 강제 로그인 처리
            return forceLogin(kakaoUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }



//    private String getAccessToken(String authCode) throws JsonProcessingException {
    private String getAccessToken(String authCode) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = configUtils.getKakaoTokenUrl();
        URI requestUrl = new URI(tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = configUtils.kakaoTokenBody(authCode);
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
            new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private String getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();
        System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        return nickname;
    }

    private User registerKakaoUserIfNeeded(String email) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByUsername(email)
            .orElse(null);
        if (kakaoUser == null) {
            // 회원가입

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            String nickname = "";
            kakaoUser = new User(email, encodedPassword, nickname);
            userRepository.save(kakaoUser);

            // 사용자 포인트 부여
            int mypoint = 0;
            int postCount = 1;
            int commentCount = 5;
            int lottoCount = 1;
            Point point = new Point(mypoint, kakaoUser, postCount, commentCount, lottoCount);
            pointRepository.save(point);
        }

        return kakaoUser;
    }

    private ResponseEntity forceLogin(User kakaoUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //처음 로그인한 유저는 nickname에 빈값을 반환


        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        System.out.println("token = " + token);
        kakaoUser.registToken(token);
        userRepository.save(kakaoUser);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(kakaoUser));
    }
}
