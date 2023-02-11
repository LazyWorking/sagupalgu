package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.item.form.UsedItemSaveForm;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserSaveForm;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    //유저 목록을 반환하는 메소드
    @GetMapping
    public String users(Model model) {
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "/user/users";
    }

    //특정 유저에 대한 조회
    @GetMapping("/{userId}")
    public String usedItem(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        log.info("user: {}", user);
        model.addAttribute("user", user);
        return "user/user";
    }

    //회원 등록 창을 넘겨준다.
    @GetMapping("/add")
    public String addForm(Model model) {
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("user", new User());

        return "user/addUserForm";
    }

    //상품을 등록하는 로직
    @PostMapping("/add")
    public String addUsedItem(@Validated @ModelAttribute("user") UserSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저를 등록하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/addUserForm";
        }

        Long savedUserId = userService.save(form);
        log.info("savedID: {}", savedUserId);
        redirectAttributes.addAttribute("userId", savedUserId);

        return "redirect:/user/{userId}";
    }

    //기존 유저에 대한 수정을 담당하는 로직
    @GetMapping("/{userId}/edit")
    public String editForm(@PathVariable Long userId, Model model) {
        //기존에 등록한 아이템을 찾아서 해당 정보를 수정 화면에 보여준다.
        User user = userService.findUser(userId);

        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("user", user);
        return "user/userEditForm";
    }

    //상품을 수정하는 로직
    @PostMapping("/{userId}/edit")
    public String editUsedItem(@Validated @ModelAttribute("user") UserEditForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 유저를 수정하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/userEditForm";
        }

        Long savedUserId = userService.editUserInfo(form);
        redirectAttributes.addAttribute("usedItemId", savedUserId);

        return "redirect:/user/{savedUserId}";
    }

    //유저를 삭제하기 위한 로직
    @GetMapping("/{userId}/delete")
    public String deleteUsedItem(@PathVariable Long userId, Model model) {
        User user = userService.findUser(userId);
        model.addAttribute("user", user);
        log.info("deletedUser:{}", user);
        return "user/deleteForm";
    }

    @PostMapping("/{userId}/delete")
    public String deleteUsedItem(@PathVariable Long userId, @ModelAttribute("user") UsedItemEditForm form,BindingResult bindingResult) {
        userService.deleteUser(form.getId());
        return "redirect:/users";
    }

}
