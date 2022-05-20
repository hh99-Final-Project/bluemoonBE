package com.sparta.bluemoon.security;

import com.sparta.bluemoon.security.jwt.JwtTokenUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

///안쓰고 있음
//로그인 성공시 토큰 내려주는 곳
public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "RefreshToken";
    public static final String TOKEN_TYPE = "BEARER";


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateAccessToken(userDetails);
        final String refreshToken = JwtTokenUtils.generaterefreshToken(userDetails);

        //실제 발급
        System.out.println("token = " + token);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(REFRESH_HEADER, TOKEN_TYPE + " " + refreshToken);
        //TODO redis에 저장하기
        //꼭!!!!controller Login 참고
    }
}


//Controller Login 참고