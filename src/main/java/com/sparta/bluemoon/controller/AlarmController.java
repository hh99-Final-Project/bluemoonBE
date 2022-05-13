package com.sparta.bluemoon.controller;

import com.sparta.bluemoon.dto.response.AlarmDto;
import com.sparta.bluemoon.security.UserDetailsImpl;
import com.sparta.bluemoon.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/api/alarm/{pageId}")
    public List<AlarmDto> getAlarms(@PathVariable int pageId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        pageId -= 1;
        return alarmService.getAlarms(pageId, userDetails.getUser());
    }
}
