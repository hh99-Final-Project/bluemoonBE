package com.sparta.bluemoon.lot;

import com.sparta.bluemoon.lot.requestDto.PersonalInfoRequestDto;
import com.sparta.bluemoon.lot.responseDto.LotResponseDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LotController {
    private final LotService lotService;
    private final LotRepository lotRepository;

    //당첨 결과
    @GetMapping("/api/lot")
    public LotResponseDto lotResult(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ///
        return lotService.doLot(userDetails.getUser());
    }

    //당첨시 개인정보 적는 칸
    @PutMapping("/api/lot/info")
    public void writePersonalInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestBody PersonalInfoRequestDto requestDto){


        lotService.writePersonalInfo(userDetails.getUser(), requestDto);

    }

}
