package com.lazyworking.sagupalgu.login.controller;

import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    //성별에 대한 목록 생성
    @ModelAttribute("genders")
    public Gender[] genders() {
        Gender[] genders = Gender.values();
        for (Gender gender : genders) {
            log.info("gender: {}, code: {},value: {}", gender, gender.getCode(), gender.getValue());
        }

        return Gender.values();
    }

    //회원 등록 창을 띄운다.
    @GetMapping("/signIn")
    public String signInForm(Model model) {
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("user", new SignInForm());
        return "login/signInForm";
    }

    //회원을 등록하는 로직
    @PostMapping("/signIn")
    public String signIn(@Validated @ModelAttribute("user") SignInForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저를 등록하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "login/signInForm";
        }

        //중복된 이메일인 경우 에러 처리
        try {
            Long savedUserId = userService.addUser(form);
            log.info("savedID: {}", savedUserId);

        } catch (IllegalStateException exception) {
            bindingResult.rejectValue("email","duplicateEmail");
            return "login/signInForm";
        }
        return "redirect:/";
    }


    @RequestMapping(value={"/login","api/login"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "login/loginForm";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping(value="/denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception, Principal principal, Model model) throws Exception {

        User user = (User)((UsernamePasswordAuthenticationToken)principal).getPrincipal();

        model.addAttribute("username", user.getName());
        model.addAttribute("exception", exception);

        return "error/denied";
    }


}
