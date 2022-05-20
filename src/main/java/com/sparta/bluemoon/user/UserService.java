package com.sparta.bluemoon.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sparta.bluemoon.point.Point;
import com.sparta.bluemoon.security.jwt.JwtDecoder;
import com.sparta.bluemoon.security.jwt.JwtTokenUtils;
import com.sparta.bluemoon.user.requestDto.NicknameSignupRequestDto;
import com.sparta.bluemoon.user.responseDto.NicknameSignupResponseDto;
import com.sparta.bluemoon.user.responseDto.SocialLoginResponseDto;
import com.sparta.bluemoon.user.responseDto.UserInfoDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.sparta.bluemoon.exception.ErrorCode.*;
import static com.sparta.bluemoon.security.jwt.JwtTokenUtils.CLAIM_EXPIRED_DATE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;
    private final RefreshRedisService refreshRedisService;

    public boolean isDuplicated(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    //로그인한 유저 정보 가져오기
    public UserInfoDto isLogin(UserDetailsImpl userDetails) {
        return new UserInfoDto(userDetails);
    }

    //로그인한 유저에 닉네임 정보 입력하기
    @Transactional
    public NicknameSignupResponseDto signupNickname(NicknameSignupRequestDto nicknameSignupRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );
        user.changeNickname(nicknameSignupRequestDto.getNickname());
        //TODO:여기에 넣으면 추천인 오류시 닉네임도 처음으로 되돌아감// 컨트롤러에 넣으면 어떻게 되지?
        return getEventPoint(nicknameSignupRequestDto, user);

    }


    //추천인 입력
    @Transactional
    public NicknameSignupResponseDto getEventPoint(NicknameSignupRequestDto nicknameSignupRequestDto, User user) {
        //추천인을 적었다면
        if (nicknameSignupRequestDto.getRecommender() != null && !Objects.equals(nicknameSignupRequestDto.getRecommender().trim(), "")) {
            User recommender = userRepository.findByNickname(nicknameSignupRequestDto.getRecommender()).orElseThrow(
                    () -> new CustomException(NOT_FOUND_RECOMMENDER)
            );

            if(nicknameSignupRequestDto.getRecommender().equals(user.getNickname())){
                throw new CustomException(CANNOT_RECOMMEND_YOURSELF);
            }

            Point userPoint = user.getPoint();
            Point recommenderPoint = recommender.getPoint();

            userPoint.eventPoint(userPoint.getMyPoint() + 500);
            recommenderPoint.eventPoint(recommenderPoint.getMyPoint() + 1000);
        }
        return new NicknameSignupResponseDto(user.getPoint());
    }




    public ResponseEntity updateAccessToken(String refreshToken){
        //리프레시토큰 만료시간이 지나지 않았을 경우

        DecodedJWT decodedJWT = jwtDecoder.isValidToken(refreshToken)
                .orElseThrow(() -> new CustomException(DONT_USE_THIS_TOKEN));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        if(!expiredDate.before(new Date())){
            System.out.println("ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ");
            String userId = refreshRedisService.getValues(refreshToken);
            User User = userRepository.findById(Long.parseLong(userId)).orElseThrow(
                    ()-> new CustomException(NOT_FOUND_USER)
            );
            UserDetailsImpl userDetails = new UserDetailsImpl(User);

            //액세스 토큰 생성
            final String token = JwtTokenUtils.generateAccessToken(userDetails);
            System.out.println("새로운 액세스 "+token);

            userRepository.save(User);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer "+token);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new SocialLoginResponseDto(User));
        } else {
            System.out.println("?????????????????");
            refreshRedisService.delValues(refreshToken);
            throw new CustomException(REFRESH_TOKEN_IS_EXPIRED);
            //로그인 페이지
        }

    }
}
