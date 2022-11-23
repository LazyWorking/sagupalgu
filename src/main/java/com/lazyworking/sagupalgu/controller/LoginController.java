package com.lazyworking.sagupalgu.controller;

import com.lazyworking.sagupalgu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/signIn")
    public String login(){
        return "/login/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    @ResponseBody
    public HashMap<String, String> kakaoLogin(@RequestParam String code) {
        return userService.kakaoLogin(code);
//        https://kauth.kakao.com/oauth/authorize?client_id=0455db792907e6e653f93d1f9daf65a7&redirect_uri=http://localhost:8080/login/oauth2/code/kakao&response_type=code
        }

    @GetMapping("/login/oauth2/code/naver")
    @ResponseBody
    public HashMap<String, String> naverLogin(@RequestParam String code){
        return userService.naverLogin(code);
//        https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=I7UExJKBf_S3n8z2QDw3&redirect_uri=http://localhost:8080/login/oauth2/code/naver&state=
    }
}
