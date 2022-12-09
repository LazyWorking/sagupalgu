package com.lazyworking.sagupalgu.user.controller;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.user.service.BlockedUsersService;
import com.lazyworking.sagupalgu.user.service.ReportedUsersService;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminUsersController {
    private final UserService userService;
    private final ReportedUsersService reportedUsersService;
    private final BlockedUsersService blockedUsersService;

    @PostConstruct
    public void afterConstruct(){
        System.out.println("admin controller");
    }
    @GetMapping
    public String getAdminPage(){
        System.out.println("hello");
        log.info("adminPage");
        return "/admin/adminPage";
    }
    
    //회원 관리
   /* @GetMapping("/users")
    public String getUserList(Model model){
        List<User> users=userService.getUsers();
        model.addAttribute("users", users);
    }
    @GetMapping("/users/{user}")
    public String getUserInfo(Model model){

    }
    */

    //신고된 회원 목록
    @GetMapping("/reportedUsers")
    public String getReportedUserList(Model model){
        List<ReportedUserDTO> reportedUsers=reportedUsersService.getReportedUsers();
        model.addAttribute("reportedUsers", reportedUsers);
        return "admin/reportedUsers";
    }
    //신고된 유저의 상세 항목
    @GetMapping("/reportedUser/{userId}")
    public String getReportedUserList(@PathVariable(name="userId") Users user, Model model) {
        //Users user = userService.findByUserId(userId);
        List<ReportedUsers> reportedUserDatas = reportedUsersService.getReportedUser(user);
        model.addAttribute("userId", user.getId());
        model.addAttribute("reportedUserDatas", reportedUserDatas);
        log.info("user : {}", user);
        log.info("reportedUser: {}", reportedUserDatas);
        return "admin/reportedUser";
    }

    //차단된 회원 목록
    @GetMapping("/blockedUsers")
    public String getBlockedUsersList(Model model){
        List<BlockedUsers> blockedUsers = blockedUsersService.getBlockedUsers();
        model.addAttribute("blockedUsers", blockedUsers);
        return "admin/blockedUsers";
    }

    @PostMapping("/blockUser/{userId}")
    public String blockUser(@PathVariable(name="userId") Users user ) {
        //Users user = userService.findByUserId(userId);
        BlockedUsers blockUser = new BlockedUsers();
        blockUser.setDate(LocalDateTime.now());
        blockUser.setUser(user);
        blockedUsersService.blockUser(blockUser);

        return "redirect:/admin/reportedUsers";
    }

    @PostMapping("/freeUser/{blockedUserId}")
    public String freeUser(@PathVariable Long blockedUserId) {
        blockedUsersService.freeUser(blockedUserId);
        return "redirect:/admin/blockedUsers";
    }
}
