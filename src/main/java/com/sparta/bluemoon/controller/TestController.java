package com.sparta.bluemoon.controller;

import static com.sparta.bluemoon.exception.ErrorCode.DOESNT_EXIST_POST_FOR_ANONYMOUS;

import com.sparta.bluemoon.exception.CustomException;
import com.sparta.bluemoon.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RedisRepository redisRepository;

    @GetMapping("/test/get")
    public void getTest() {
        System.out.println("HIHIHI");
        throw new CustomException(DOESNT_EXIST_POST_FOR_ANONYMOUS);
//        redisRepository.getTest();
    }

    @GetMapping("/test/put")
    public void putTest() {
        redisRepository.putTest();
    }

    @GetMapping("/test/del")
    public void delTest() {
        redisRepository.delTest();
    }
}
