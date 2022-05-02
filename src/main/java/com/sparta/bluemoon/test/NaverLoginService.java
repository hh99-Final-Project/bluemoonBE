package com.sparta.bluemoon.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.response.SocialLoginResponseDto;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.security.jwt.JwtTokenUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final ConfigUtils configUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> requestAuthCodeFromNaver() {
        String authUrl = configUtils.naverInitUrl();
        System.out.println(authUrl);
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

//    public ResponseEntity<NaverLoginDto> login(String authCode) {
    public ResponseEntity login(String authCode) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
            String jwtToken = getAccessToken(authCode);
            String email = getNaverUserInfo(jwtToken);
            User naverUser = registerNaverUserIfNeeded(email);
            return forceLogin(naverUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    private ResponseEntity forceLogin(User naverUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(naverUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // toDo: random Nickname 추가해 줘야함
        String nickname = "닉네임 넣어줄 예정이에요";
        naverUser.createNickname(nickname);

        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        System.out.println("token = " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(naverUser));
    }

    private User registerNaverUserIfNeeded(String email) {
        User naverUser = userRepository.findByUsername(email)
            .orElse(null);
        if (naverUser == null) {
            // 회원가입

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            naverUser = new User(email, encodedPassword);
            userRepository.save(naverUser);
        }
        return naverUser;

    }

    private String getNaverUserInfo(String jwtToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.POST,
            naverUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response").get("nickname").asText();
        String email = jsonNode.get("response").get("email").asText();
        String name = jsonNode.get("response").get("name").asText();

        System.out.println("id = " + id);
        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);
        System.out.println("name = " + name);
//        String nickname = jsonNode.get("properties")
//            .get("nickname").asText();
//        String email = jsonNode.get("naver_account")
//            .get("email").asText();
//
//        return new NaverUserInfoDto(id, nickname, email);
        return email;
    }

//    private String getAccessToken(String authCode) throws JsonProcessingException {
    private String getAccessToken(String authCode) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = configUtils.naverTokenUrl(authCode);
        System.out.println("tokenUrl = " + tokenUrl);
        URI requestUrl = new URI(tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }
}
