package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.response.LotResponseDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LotController {
    private final LotService lotService;

    @GetMapping("/api/lot")
    public LotResponseDto lotResult(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ///
        if(userDetails.getUser()==null){
            System.out.println("유저 못찾음");
        }
        System.out.println("유저아이디"+userDetails.getUser().getId());
        return lotService.doLot(userDetails.getUser());

    }

}
