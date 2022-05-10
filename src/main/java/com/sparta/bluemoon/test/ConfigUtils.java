package com.sparta.bluemoon.test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigUtils {
    @Value("${naver.auth.url}")
    private String naverAuthUrl;

    @Value("${naver.response.type}")
    private String responseType;

    @Value("${naver.redirect.uri}")
    private String naverRedirectUrl;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.secret}")
    private String naverSecret;

    @Value("${naver.state}")
    private String state;

    @Value("${naver.grant.type}")
    private String grantType;

	// Naver code 요청 URL 생성 로직
    public String naverInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getNaverClientId());
        params.put("response_type", getResponseType());
        params.put("redirect_uri", getNaverRedirectUrl());
        params.put("state", getState());

        String paramStr = params.entrySet().stream()
            .map(param -> param.getKey() + "=" + param.getValue())
            .collect(Collectors.joining("&"));

        return getNaverAuthUrl()
                + "/authorize"
                + "?"
                + paramStr;
    }

    public String naverTokenUrl(String authCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", getGrantType());
        params.put("client_id", getNaverClientId());
        params.put("client_secret", getNaverSecret());
        params.put("code", authCode);
        params.put("state", getState());

        String paramStr = params.entrySet().stream()
            .map(param -> param.getKey() + "=" + param.getValue())
            .collect(Collectors.joining("&"));

        return getNaverAuthUrl()
            + "/token"
            + "?"
            + paramStr;
    }
}