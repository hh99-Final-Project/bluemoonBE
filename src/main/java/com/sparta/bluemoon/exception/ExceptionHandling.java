package com.sparta.bluemoon.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandling{

    Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        logger.error("에러가 발생했어요");
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}