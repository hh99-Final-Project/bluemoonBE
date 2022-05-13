package com.sparta.bluemoon.service;

import com.sparta.bluemoon.domain.Point;
import com.sparta.bluemoon.domain.User;
import com.sparta.bluemoon.dto.request.NicknameSignupRequestDto;
import com.sparta.bluemoon.dto.response.NicknameSignupResponseDto;
import com.sparta.bluemoon.dto.response.UserInfoDto;
import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.UserRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.bluemoon.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        if (nicknameSignupRequestDto.getRecommender() != null) {
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
}
