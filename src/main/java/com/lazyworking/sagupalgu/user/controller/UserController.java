package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.global.security.service.AuthenticationUtils;
import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.*;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //회원 신고창 반환
    @GetMapping("/report")
    public String reportUserForm(Model model) {
        model.addAttribute("form", new ReportUserForm());
        return "user/reportUserForm";
    }

    //회원을 신고처리 로직
    @PostMapping("/report")
    public String addUser(@Validated @ModelAttribute("form") ReportUserForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저를 등록하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/reportUserForm";
        }

        User user = AuthenticationUtils.getUserFromSecurityContext();
        form.setReporterUserId(user.getId());

        try{
            userService.reportUser(form);
        }
        catch(IllegalStateException exception){
            //신고 대상자가 올바르지 않는 경우
            bindingResult.rejectValue("targetUserEmail", "no_target_user");
            return "user/reportUserForm";
        }
        return "redirect:/";
    }


}
