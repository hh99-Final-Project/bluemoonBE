package com.sparta.bluemoon.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sparta.bluemoon.security.UserDetailsImpl;
import java.util.Date;

public final class JwtTokenUtils {

    //토큰에 넣을 정보들
    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    // JWT 토큰의 유효기간: 30분 (단위: seconds)
    private static final int JWT_TOKEN_VALID_SEC = 30 * MINUTE;
    // JWT 토큰의 유효기간: 3일 (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000;

    // JWT 토큰의 유효기간: 3일 (단위: seconds)
    private static final int REFRESH_TOKEN_VALID_SEC = 3 * DAY;  // 5 * MINUTE; //
    // JWT 토큰의 유효기간: 3일 (단위: milliseconds)
    private static final int REFRESH_TOKEN_VALID_MILLI_SEC = REFRESH_TOKEN_VALID_SEC * 1000;

    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CLAIM_USER_NAME = "USER_NAME";
    public static final String JWT_SECRET = "jwt_secret_!@#$%";
    public static final String CLAIM_NICKNAME = "NICKNAME";
    public static final String CLAIM_USER_ID = "USER_ID";

    //액세스토큰 생성
    public static String generateAccessToken(UserDetailsImpl userDetails) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("sparta")
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_NICKNAME, userDetails.getUser().getNickname())
                    .withClaim(CLAIM_USER_ID, userDetails.getUser().getId())
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());

        } catch (Exception e) {

        }
        System.out.println(token);
        return token;
    }


    //리프레시토큰 생성
    public static String generaterefreshToken(UserDetailsImpl userDetails) {
        String refreshToken = null;
        try {
            refreshToken = JWT.create()
                    .withIssuer("sparta")
                    .withClaim(CLAIM_USER_ID, userDetails.getUser().getId())
                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());

        } catch (Exception e) {

        }
        System.out.println(refreshToken);
        return refreshToken;
    }

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }
}
