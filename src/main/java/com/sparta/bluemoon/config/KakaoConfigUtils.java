package com.sparta.bluemoon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class KakaoConfigUtils {
    @Value("${kakao.auth.url}")
    private String kakaoAuthUrl;

    @Value("${kakao.response.type}")
    private String responseType;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUrl;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.grant.type}")
    private String grantType;

	// Kakao code 요청 URL 생성 로직
    public String kakaoInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getKakaoClientId());
        params.put("response_type", getResponseType());
        params.put("redirect_uri", getKakaoRedirectUrl());

        String paramStr = params.entrySet().stream()
            .map(param -> param.getKey() + "=" + param.getValue())
            .collect(Collectors.joining("&"));

        return getKakaoAuthUrl()
                + "/authorize"
                + "?"
                + paramStr;
    }

    public MultiValueMap<String, String> kakaoTokenBody(String authCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", getGrantType());
        body.add("client_id", getKakaoClientId());
        body.add("redirect_uri", getKakaoRedirectUrl());
        body.add("code", authCode);

        return body;
    }

    public String getKakaoTokenUrl() {
        return getKakaoAuthUrl() + "/token";
    }
}
