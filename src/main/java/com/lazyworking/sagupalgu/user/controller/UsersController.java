package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @GetMapping("/signIn")
    public String login(){
        return "/login/login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(HttpServletResponse response, @RequestParam String code) {
        HashMap<String, Boolean> userMap = userService.kakaoLogin(response, code);
        if(userMap.get("newUser")){
            return "/login/nickName";
        }
        return "redirect:/";
//        https://kauth.kakao.com/oauth/authorize?client_id=0455db792907e6e653f93d1f9daf65a7&redirect_uri=http://localhost:8080/login/oauth2/code/kakao&response_type=code
    }

    @GetMapping("/login/oauth2/code/naver")
    public String naverLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String code){
        HashMap<String, Boolean> userMap = userService.naverLogin(response, code);
        if(userMap.get("newUser")){
            return "/login/nickName";
        }
        return "redirect:/";
//        https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=I7UExJKBf_S3n8z2QDw3&redirect_uri=http://localhost:8080/login/oauth2/code/naver&state=
    }

    @GetMapping("/login/oauth2/setNickName")
    public String getNickName(HttpServletRequest request){
        return "/login/nickName";
    }

    @PostMapping("/login/oauth2/setNickName")
    public String setNickName(HttpServletRequest request, Model model, @RequestParam(value="nickName")String nickName){
        userService.setNickName(request, nickName);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(){
        return "/index/index";
    }
}
