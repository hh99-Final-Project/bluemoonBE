package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.domain.Lot;
import com.sparta.bluemoon.dto.request.PersonalInfoRequestDto;
import com.sparta.bluemoon.dto.response.LotResponseDto;
import com.sparta.bluemoon.repository.LotRepository;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        lotService.writePersonalInfo(userDetails, requestDto);

    }

}
