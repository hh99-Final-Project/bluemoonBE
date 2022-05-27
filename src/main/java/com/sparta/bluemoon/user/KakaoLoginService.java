package com.sparta.bluemoon.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.config.KakaoConfigUtils;
import com.sparta.bluemoon.point.Point;
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
    private final RefreshRedisService refreshRedisService;


    public ResponseEntity login(String jwtToken) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
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
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        return nickname;
    }

    private User registerKakaoUserIfNeeded(String email) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakao = "kakao";
        User kakaoUser = userRepository.findByUsernameAndType(email, kakao)
            .orElse(null);
        if (kakaoUser == null) {
            // 회원가입

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String type = "kakao";

            String nickname = "";
            kakaoUser = new User(email, encodedPassword, nickname, type);
            userRepository.save(kakaoUser);

            // 사용자 포인트 부여
            int mypoint = 0;
            int postCount = 1;
            int commentCount = 5;
            int lottoCount = 1;
            int recommendCount = 0;
            Point point = new Point(mypoint, kakaoUser, postCount, commentCount, lottoCount, recommendCount);
            pointRepository.save(point);
        }

        return kakaoUser;
    }

    private ResponseEntity forceLogin(User kakaoUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Token 생성
        final String token = JwtTokenUtils.generateAccessToken(userDetails);
        final String refreshToken = JwtTokenUtils.generaterefreshToken(userDetails);
        System.out.println("token = " + token);

        userRepository.save(kakaoUser);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        headers.set("RefreshToken","Bearer "+refreshToken);

        //redis에저장
        refreshRedisService.setValues(refreshToken,String.valueOf(kakaoUser.getId()));

        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(kakaoUser));

    }
}
