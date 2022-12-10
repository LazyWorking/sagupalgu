package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.form.UsedItemSaveForm;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.user.form.ReportUserForm;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsersController {
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
    //유저 신고하기 기능
    @GetMapping("/user/report")
    public String getReportForm(Model model) {
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("reportUser", new ReportUserForm());

        return "user/reportForm";
    }
    @PostMapping("/user/report")
    public String getReportForm(@Validated @ModelAttribute("reportUser") ReportUserForm reportUserForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {


        String id = reportUserForm.getId();
        /*
        신고 접수된 id가 DB에 있는지 여부를 검사한다.
        bindingResult.rejectValue("id","exists")
         */

        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저 신고하기 창으로 돌아간다.
        if (bindingResult.hasErrors()) {
            return "user/reportForm";
        }
        /*
        신고 처리

         */

        return "/";
    }
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "hello";
    }
}
