package com.sparta.bluemoon.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bluemoon.user.User;
import com.sparta.bluemoon.exception.Exception;
import com.sparta.bluemoon.user.UserRepository;
import com.sparta.bluemoon.security.jwt.HeaderTokenExtractor;
import com.sparta.bluemoon.security.jwt.JwtDecoder;
import com.sparta.bluemoon.security.jwt.JwtPreProcessingToken;
import java.io.IOException;
import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Token 을 내려주는 Filter 가 아닌  client 에서 받아지는 Token 을 서버 사이드에서 검증하는 클레스 SecurityContextHolder 보관소에 해당
 * Token 값의 인증 상태를 보관 하고 필요할때 마다 인증 확인 후 권한 상태 확인 하는 기능
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final HeaderTokenExtractor extractor;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    public JwtAuthFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            HeaderTokenExtractor extractor,
            JwtDecoder jwtDecoder, UserRepository userRepository) {
        super(requiresAuthenticationRequestMatcher);

        this.extractor = extractor;
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException {


        // JWT 값을 담아주는 변수 TokenPayload
        String tokenPayload = request.getHeader("Authorization");

        if (tokenPayload == null) {
            System.out.println("tokenpayload null????????");
            System.out.println(tokenPayload);

//            response.sendRedirect("/user/loginView");
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8"); // HelloData 객체
            Exception exception = new Exception();
            exception.setHttpStatus(HttpStatus.BAD_REQUEST);
            exception.setErrorMessage("혜미님 잘 가나요?");
            String result = mapper.writeValueAsString(exception);
            response.getWriter().print(result);
            return null;
        }

        System.out.println("tokenPayload = " + tokenPayload);

        String nowToken = extractor.extract(tokenPayload, request);
        JwtPreProcessingToken jwtToken = new JwtPreProcessingToken(
                extractor.extract(tokenPayload, request));

        String username = jwtDecoder.decodeUsername(nowToken);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 없습니다")
        );
        if(!user.getToken().equals(nowToken)){

            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8"); // HelloData 객체
            Exception exception = new Exception();
            exception.setAlreadyLogined(true);
            String result = mapper.writeValueAsString(exception);
            response.getWriter().print(result);
            return null;
        }
        return super
                .getAuthenticationManager()
                .authenticate(jwtToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        /*
         *  SecurityContext 사용자 Token 저장소를 생성합니다.
         *  SecurityContext 에 사용자의 인증된 Token 값을 저장합니다.
         */
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // FilterChain chain 해당 필터가 실행 후 다른 필터도 실행할 수 있도록 연결실켜주는 메서드
        chain.doFilter(
                request,
                response
        );
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        /*
         *	로그인을 한 상태에서 Token값을 주고받는 상황에서 잘못된 Token값이라면
         *	인증이 성공하지 못한 단계 이기 때문에 잘못된 Token값을 제거합니다.
         *	모든 인증받은 Context 값이 삭제 됩니다.
         */
        SecurityContextHolder.clearContext();

        super.unsuccessfulAuthentication(
                request,
                response,
                failed
        );
    }
}
