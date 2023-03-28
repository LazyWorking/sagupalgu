package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.*;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
//@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
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
    
    //특정 유저에 대한 조회
    @GetMapping("/{userId}")
    public String usedItem(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        log.info("user: {}", user);
        model.addAttribute("user", user);
        log.info("gender:{},gender:{}, class:{}", user.getGender().getCode(), user.getGender().getValue(), user.getGender().getClass());
        return "user/user";
    }

    //회원 수정창을 띄운다.
    @GetMapping("/{userId}/edit")
    public String editForm(@PathVariable Long userId, Model model) {
        //기존에 등록한 아이템을 찾아서 해당 정보를 수정 화면에 보여준다.
        User user = userService.findUser(userId);

        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("user", user);

        log.info("edit user: {} ", user);
        log.info("gender: {}", user.getGender());
        return "user/editUserForm";
    }

    //회원 정보 변경 처리
    @PostMapping("/{userId}/edit")
    public String editUser(@PathVariable Long userId, @Validated @ModelAttribute("user") UserEditForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            log.info("users={}", form);
            return "user/editUserForm";
        }

        Long editUserId = userService.editUserInfo(form);
        log.info("editUserId: {}", editUserId);
        redirectAttributes.addAttribute("userId", editUserId);

        return "redirect:/users/{userId}";
    }

    //회원의 비밀번호 변경창을 띄운다.
    @GetMapping("/{userId}/changePassword")
    public String changePasswordForm(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        UserPasswordForm form = new UserPasswordForm(userId);

        model.addAttribute("user", form);
        return "user/changePasswordForm";
    }

    //비밀번호를 변경하는 로직
    @PostMapping("/{userId}/changePassword")
    public String changeUserPassword(@PathVariable Long userId, @Validated @ModelAttribute("user") UserPasswordForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/changePasswordForm";
        }
        Long editUserId = userService.changePassword(form);
        redirectAttributes.addAttribute("userId", editUserId);

        return "redirect:/users/{userId}/edit";
    }

    //회원 삭제 창을 띄운다.
    @GetMapping("/{userId}/delete")
    public String deleteUsedItem(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        model.addAttribute("user", user);
        log.info("deletedUser:{}", user);
        return "user/deleteUserForm";
    }

    //회원 삭제 로직
    @PostMapping("/{userId}/delete")
    public String deleteUsedItem(@PathVariable Long userId, @ModelAttribute("user") UsedItemEditForm form, BindingResult bindingResult) {
        userService.deleteUser(form.getId());
        return "redirect:/users";
    }
    //회원 신고창 반환
    @GetMapping("/report")
    public String reportUserForm(Model model) {
        ReportUserForm form = new ReportUserForm(4L);
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("reportUser", form);

        return "user/reportUserForm";
    }

    //회원을 신고처리 로직
    @PostMapping("/report")
    public String addUser(@Validated @ModelAttribute("reportUser") ReportUserForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저를 등록하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/reportUserForm";
        }
        userService.reportUser(form);
        return "redirect:/";
    }

}
