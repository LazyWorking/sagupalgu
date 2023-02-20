package com.lazyworking.sagupalgu.login.controller;

import com.lazyworking.sagupalgu.login.service.LoginService;
import com.lazyworking.sagupalgu.login.form.LoginForm;
import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;

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
            return "user/addUserForm";
        }

        Long savedUserId = loginService.signIn(form);
        log.info("savedID: {}", savedUserId);

        return "redirect:/";
    }


    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute("user", new LoginForm());
        return "login/loginForm";
    }

    //회원 로그인 처리 로직
    @PostMapping("/login")
    public String loginForm(@Validated @ModelAttribute("user") LoginForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
            return "login/loginForm";
        }

        //로그인 에러
        if (!loginService.login(form)) {
            log.info("form: {}, login failure", form);
            bindingResult.reject("login_error");
            return "login/loginForm";
        }
        return "redirect:/";
    }


}
