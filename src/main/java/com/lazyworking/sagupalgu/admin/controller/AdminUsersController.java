package com.lazyworking.sagupalgu.admin.controller;

import com.lazyworking.sagupalgu.global.security.service.SecurityResourceService;
import com.lazyworking.sagupalgu.admin.domain.Resource;
import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.form.ResourceAddForm;
import com.lazyworking.sagupalgu.admin.form.ResourceForm;
import com.lazyworking.sagupalgu.admin.service.ResourceService;
import com.lazyworking.sagupalgu.admin.service.RoleHierarchyService;
import com.lazyworking.sagupalgu.admin.service.RoleService;
import com.lazyworking.sagupalgu.user.domain.*;
import com.lazyworking.sagupalgu.admin.form.UserAllDataForm;
import com.lazyworking.sagupalgu.admin.form.UserManageForm;
import com.lazyworking.sagupalgu.user.service.BlockedUsersService;
import com.lazyworking.sagupalgu.user.service.ReportedUsersService;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminUsersController {
    private final UserService userService;
    private final ReportedUsersService reportedUsersService;
    private final BlockedUsersService blockedUsersService;
    private final RoleService roleService;
    private final ResourceService resourceService;
    private final RoleHierarchyService roleHierarchyService;

    private final SecurityResourceService securityResourceService;

    //권한에 대한 목록 생성
    @ModelAttribute("roles")
    public List<Role> roles(){
        List<Role> roles = roleService.findAllRoles();
        log.info("roles: {}", roles);
        return roles;
    }
    //성별에 대한 목록 생성
    @ModelAttribute("genders")
    public Gender[] genders() {
        Gender[] genders = Gender.values();
        for (Gender gender : genders) {
            log.info("gender: {}, code: {},value: {}", gender, gender.getCode(), gender.getValue());
        }

        return Gender.values();
    }
    //관리자 페이지 반환
    @GetMapping("")
    public String adminPage() {
        return "/admin/adminPage";
    }


    //유저 목록을 반환하는 메소드
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
        return "/admin/usercontrol/users";
    }

    //관리자 전용 유저 상세
    @GetMapping("/users/{userId}")
    public String user(@PathVariable("userId") Long userId,Model model) {
        UserAllDataForm user = userService.findUserAllDataForm(userId);
        model.addAttribute("user", user);
        log.info("user:{}", user);
        return "/admin/usercontrol/user";
    }

    //관리자 전용 유저 정보 변경
    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId") Long userId,Model model) {
        UserManageForm userManageForm = userService.findUserManageForm(userId);
        model.addAttribute("user", userManageForm);
        log.info("user:{}", userManageForm);
        return "/admin/usercontrol/manageUserForm";
    }

    @PostMapping("/users/{userId}/edit")
    public String editUser(@PathVariable("userId") Long userId, @Validated @ModelAttribute("user") UserManageForm userManageForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
            return "/admin/usercontrol/manageUserForm";
        }
        log.info("editedUser:{}", userManageForm);
        Long editUserId = userService.editUserInfo_manage(userManageForm);
        log.info("editUserId: {}", editUserId);
        redirectAttributes.addAttribute("userId", editUserId);

        return "redirect:/admin/users/{userId}";
    }

    //신고된 회원 목록
    @GetMapping("/users/reportedUsers")
    public String getReportedUserList(Model model){
        List<ReportedUserDTO> reportedUsers=reportedUsersService.getReportedUsers();
        model.addAttribute("reportedUsers", reportedUsers);
        return "admin/usercontrol/reportedUsers";
    }

    //신고된 유저의 상세 항목
    @GetMapping("/users/reportedUser/{userId}")
    public String getReportedUserInfo(@PathVariable Long userId, Model model) {
        List<ReportedUsers> reportedUsers = reportedUsersService.getTargetUser(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("reportedUsers", reportedUsers);
        log.info("user : {}", userId);
        log.info("reportedUser: {}", reportedUsers);
        return "admin/usercontrol/reportedUser";
    }

    //차단된 회원 목록
    @GetMapping("/users/blockedUsers")
    public String getBlockedUsersList(Model model){
        List<BlockedUsers> blockedUsers = blockedUsersService.getBlockedUsers();
        model.addAttribute("blockedUsers", blockedUsers);
        return "admin/usercontrol/blockedUsers";
    }

    @PostMapping("/users/blockUser/{userId}")
    public String blockUser(@PathVariable long userId) {
        blockedUsersService.blockUser(userId);
        return "redirect:/admin/users/blockedUsers";
    }

    @PostMapping("/users/freeUser/{blockedUserId}")
    public String freeUser(@PathVariable Long blockedUserId) {
        blockedUsersService.freeUser(blockedUserId);
        return "redirect:/admin/users/blockedUsers";
    }
}
