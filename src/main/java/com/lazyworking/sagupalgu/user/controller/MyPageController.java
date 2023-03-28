package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.global.security.service.AccountContext;
import com.lazyworking.sagupalgu.global.security.service.CustomUserDetailsService;
import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.ReportUserForm;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserPasswordForm;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users/myPage")
public class MyPageController {
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    //성별에 대한 목록 생성
    @ModelAttribute("genders")
    public Gender[] genders() {
        Gender[] genders = Gender.values();
        for (Gender gender : genders) {
            log.info("gender: {}, code: {},value: {}", gender, gender.getCode(), gender.getValue());
        }

        return Gender.values();
    }

    //마이페이지 반환
    @GetMapping("")
    public String myPage() {
        return "/user/myPage";
    }
    
    //특정 유저에 대한 조회
    @GetMapping("/userInfo")
    public String usedItem(Model model) {
        User user = getUserFromSecurityContext();
        log.info("user: {}", user);
        model.addAttribute("user", user);
        log.info("gender:{},gender:{}, class:{}", user.getGender().getCode(), user.getGender().getValue(), user.getGender().getClass());
        return "user/user";
    }

    //회원 수정창을 띄운다.
    @GetMapping("/userInfo/edit")
    public String editForm(Model model) {
        //기존에 등록한 아이템을 찾아서 해당 정보를 수정 화면에 보여준다.
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("user", user);

        log.info("edit user: {} ", user);
        log.info("gender: {}", user.getGender());
        return "user/editUserForm";
    }

    //회원 정보 변경 처리
    @PostMapping("/userInfo/edit")
    public String editUser(@Validated @ModelAttribute("user") UserEditForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            log.info("users={}", form);
            return "user/editUserForm";
        }
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        Long editUserId = userService.editUserInfo(form);
        setNewContext(form.getId());
        log.info("editUserId: {}", editUserId);
        redirectAttributes.addAttribute("userId", editUserId);
        return "redirect:/users/myPage/userInfo";
    }

    //회원의 비밀번호 변경창을 띄운다.
    @GetMapping("/userInfo/changePassword")
    public String changePasswordForm(Model model) {
        User user = getUserFromSecurityContext();
        UserPasswordForm form = new UserPasswordForm(user.getId());
        model.addAttribute("user", form);
        return "user/changePasswordForm";
    }

    //비밀번호를 변경하는 로직
    @PostMapping("/userInfo/changePassword")
    public String changeUserPassword(@Validated @ModelAttribute("user") UserPasswordForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/changePasswordForm";
        }
        form.setPassword(passwordEncoder.encode(form.getPassword()));
        Long editUserId = userService.changePassword(form);
        setNewContext(editUserId);
        redirectAttributes.addAttribute("userId", editUserId);

        return "redirect:/users/myPage/userInfo/edit";
    }

    //회원 삭제 창을 띄운다.
    @GetMapping("/userInfo/delete")
    public String deleteUsedItem(Model model) {
        User user = getUserFromSecurityContext();
        model.addAttribute("user", user);
        log.info("deletedUser:{}", user);
        return "user/deleteUserForm";
    }

    //회원 삭제 로직
    @PostMapping("/userInfo/delete")
    public String deleteUsedItem(@ModelAttribute("user") UsedItemEditForm form, BindingResult bindingResult) {
        userService.deleteUser(form.getId());
        return "redirect:/logout";
    }
    private void setNewContext(Long id) {
        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserById(id);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(accountContext.getUser(), null, accountContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
