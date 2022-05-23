package com.sparta.bluemoon.user;

import com.sparta.bluemoon.user.requestDto.NicknameCheckRequestDto;
import com.sparta.bluemoon.user.requestDto.NicknameSignupRequestDto;
import com.sparta.bluemoon.user.requestDto.RefreshTokenDto;
import com.sparta.bluemoon.user.responseDto.NicknameSignupResponseDto;
import com.sparta.bluemoon.user.responseDto.UserInfoDto;
import com.sparta.bluemoon.point.PointRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인한 유저 정보 가져오기
    @GetMapping("/api/user/islogin")
    public UserInfoDto isLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.isLogin(userDetails);
    }

    // 닉네임 중복 확인
    @PostMapping("/api/nicknames")
    public boolean isDuplicated(@RequestBody NicknameCheckRequestDto nicknameCheckRequestDto) {
        return userService.isDuplicated(nicknameCheckRequestDto.getNickname());
    }

    //로그인한 유저에 닉네임 정보 입력하기
    @PostMapping("/api/user/nickname")
    public NicknameSignupResponseDto signupNickname(@RequestBody NicknameSignupRequestDto nicknameSignupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.signupNickname(nicknameSignupRequestDto, userDetails);
    }

    //리프레쉬 토큰 검증
    @PostMapping("/api/refresh")
    public ResponseEntity loginByRefreshToken(@RequestHeader("RefreshToken") String refreshToken){
        return userService.updateAccessToken(refreshToken.substring(7));
    }
}
