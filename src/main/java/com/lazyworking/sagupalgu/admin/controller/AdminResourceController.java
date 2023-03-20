package com.lazyworking.sagupalgu.admin.controller;

import com.lazyworking.sagupalgu.global.security.service.SecurityResourceService;
import com.lazyworking.sagupalgu.admin.domain.Resource;
import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.form.ResourceAddForm;
import com.lazyworking.sagupalgu.admin.form.ResourceForm;
import com.lazyworking.sagupalgu.admin.service.ResourceService;
import com.lazyworking.sagupalgu.admin.service.RoleService;
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
@RequestMapping("/admin/resources")
@RequiredArgsConstructor
@Slf4j
public class AdminResourceController {
    private final ResourceService resourceService;
    private final RoleService roleService;
    private final SecurityResourceService securityResourceService;

    @ModelAttribute("roles")
    public List<Role> roles() {
        return roleService.findAllRoles();
    }
    //자원 관리
    @GetMapping("")
    public String resources(Model model) {
        model.addAttribute("resources",resourceService.findAllResource());
        return "/admin/resourcecontrol/resources";
    }

    //자원 상세 현황 반환
    @GetMapping("/{resourceId}")
    public String resource(@PathVariable("resourceId") Long resourceId, Model model) {
        ResourceForm resource = resourceService.getResourceForm(resourceId);
        model.addAttribute("resource", resource);
        return "/admin/resourcecontrol/resource";
    }
    //자원 수정창 반환
    @GetMapping("/{resourceId}/edit")
    public String resourceEditForm(@PathVariable("resourceId") Long resourceId, Model model) {
        ResourceForm resource = resourceService.getResourceForm(resourceId);
        model.addAttribute("resource", resource);
        return "/admin/resourcecontrol/resourceEditForm";
    }
    //자원 수정
    @PostMapping("/{resourceId}/edit")
    public String resourceEdit(@PathVariable("resourceId") Long resourceId, @Validated @ModelAttribute("resource") ResourceForm resourceForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors:{}", bindingResult);
            return "admin/resourcecontrol/resourceEditForm";
        }
        log.info("edited Resource: {}", resourceForm);
        Long editResourceId=resourceService.change(resourceForm);
        securityResourceService.loadUrlResources();
        redirectAttributes.addAttribute("resourceId", editResourceId);
        return "redirect:/admin/resources/{resourceId}";
    }//자원 등록창 반환
    @GetMapping("/add")
    public String resourceAddForm(Model model) {
        model.addAttribute("resource", new ResourceAddForm());
        return "/admin/resourcecontrol/resourceAddForm";
    }

    //자원등록 처리
    @PostMapping("/add")
    public String resourceAdd(@Validated @ModelAttribute("resource") ResourceAddForm resourceAddForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors:{}", bindingResult);
            return "admin/resourcecontrol/resourceAddForm";
        }
        log.info("added Resource: {}", resourceAddForm);
        Long resourceId=resourceService.createResource(resourceAddForm);
        securityResourceService.loadUrlResources();
        redirectAttributes.addAttribute("resourceId", resourceId);
        return "redirect:/admin/resources/{resourceId}";
    }


    //자원 삭제 창을 띄운다.
    @GetMapping("/{resourceId}/delete")
    public String deleteResourceForm(@PathVariable Long resourceId, Model model) {
        ResourceForm resource = resourceService.getResourceForm(resourceId);
        model.addAttribute("resource", resource);
        log.info("resource:{}", resource);
        return "/admin/resourcecontrol/resourceDeleteForm";
    }

    //자원 삭제 로직
    @PostMapping("/{resourceId}/delete")
    public String deleteResource(@PathVariable Long resourceId, @ModelAttribute("resource") ResourceForm resourceForm) {
        resourceService.deleteResourceById(resourceId);
        securityResourceService.loadUrlResources();
        return "redirect:/admin/resources";
    }


}
