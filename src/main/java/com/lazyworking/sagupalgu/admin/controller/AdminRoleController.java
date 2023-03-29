package com.lazyworking.sagupalgu.admin.controller;

import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.form.RoleAddForm;
import com.lazyworking.sagupalgu.admin.form.RoleForm;
import com.lazyworking.sagupalgu.admin.service.RoleHierarchyService;
import com.lazyworking.sagupalgu.admin.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Slf4j
public class AdminRoleController {
    private final RoleService roleService;
    private final RoleHierarchyService roleHierarchyService;

    //권한 관리
    @GetMapping("")
    public String resources(Model model) {
        model.addAttribute("roles",roleService.findAllRoles());
        return "/admin/rolecontrol/roles";
    }

    //권한 상세 현황 반환
    @GetMapping("/{roleId}")
    public String resource(@PathVariable("roleId") Long roleId, Model model) {
        Role role = roleService.findRoleById(roleId);
        model.addAttribute("role", role);
        return "/admin/rolecontrol/role";
    }

    //권한 수정창 반환
    @GetMapping("{roleId}/edit")
    public String resourceEditForm(@PathVariable("roleId") Long roleId, Model model) {
        Role role = roleService.findRoleById(roleId);
        model.addAttribute("role", role);
        return "/admin/rolecontrol/roleEditForm";
    }
    //권한 수정
    @PostMapping("/{roleId}/edit")
    public String roleEdit(@PathVariable("roleId") Long roleId, @Validated @ModelAttribute("resource") RoleForm roleForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors:{}", bindingResult);
            return "admin/rolecontrol/roleEditForm";
        }
        log.info("edited Role: {}", roleForm);
        Long editRoleId=roleService.change(roleForm);
        redirectAttributes.addAttribute("roleId", editRoleId);
        return "redirect:/admin/roles/{roleId}";
    }
    //권한 등록창 반환
    @GetMapping("/add")
    public String roleAddForm(Model model) {
        model.addAttribute("role", new RoleAddForm());
        return "/admin/rolecontrol/roleAddForm";
    }

    //권한 등록 처리
    @PostMapping("/add")
    public String roleAdd(@Validated @ModelAttribute("role") RoleAddForm roleAddForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors:{}", bindingResult);
            return "admin/rolecontrol/roleAddForm";
        }
        log.info("added Role: {}", roleAddForm);
        Long roleId=roleService.createRole(roleAddForm);
        redirectAttributes.addAttribute("roleId", roleId);
        return "redirect:/admin/roles/{roleId}";
    }


    //권한 삭제 창을 띄운다.
    @GetMapping("/{roleId}/delete")
    public String deleteRoleForm(@PathVariable Long roleId, Model model) {
        Role role = roleService.findRoleById(roleId);
        model.addAttribute("role", role);
        log.info("role:{}", role);
        return "admin/rolecontrol/roleDeleteForm";
    }

    //권한 삭제 로직
    @PostMapping("/{roleId}/delete")
    public String deleteRole(@PathVariable Long roleId, @ModelAttribute("role") RoleForm roleForm) {
        roleService.deleteRoleById(roleId);
        return "redirect:/admin/roles";
    }
}
