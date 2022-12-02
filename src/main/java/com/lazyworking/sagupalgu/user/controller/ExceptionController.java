package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.user.exception.AccessTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<String> accessTokenExpired(Exception e){
        log.info("{}", e.getClass());
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
