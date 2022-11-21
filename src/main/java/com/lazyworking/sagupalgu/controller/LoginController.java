package com.lazyworking.sagupalgu.controller;

import com.lazyworking.sagupalgu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/signIn")
    public String login(){
        return "/login/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam String code) {
        return userService.kakaoLogin(code);
//        https://accounts.kakao.com/login/?continue=https://kauth.kakao.com/oauth/authorize?response_type=code&redirect_uri=http://localhost:8080%/login/oauth2/code/kakao%26through_account%3Dtrue%26client_id%3D43e398315e08268714887b3750a55107    }
    }

    @GetMapping("/login/oauth2/code/naver")
    public String naverLogin(@RequestParam String code){
        return userService.naverLogin(code);
//        https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=I7UExJKBf_S3n8z2QDw3&redirect_uri=http://localhost:8080/login/oauth2/code/naver&state=
    }
}
