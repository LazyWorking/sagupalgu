package com.lazyworking.sagupalgu.item.controller;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.category.service.CategoryService;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.item.form.UsedItemSaveForm;
import com.lazyworking.sagupalgu.item.service.UsedItemService;
import com.lazyworking.sagupalgu.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
 * author: JehyunJung
 * purpose: controller for usedItem
 * version: 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/usedItems")
@Slf4j
public class UsedItemController {
    private final UsedItemService usedItemService;

    private final CategoryService categoryService;

    //Category에 대한 체크 박스를 만들기 위해 미리 사전에 Category List을 받아온다.
    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.findAllCategories();
    }

    //모든 아이템 목록을 조회에 대한 요청 처리
    @GetMapping
    public String usedItems(Model model) {
        List<UsedItem> usedItems = usedItemService.findAllUsedItems();
        model.addAttribute("usedItems", usedItems);
        return "/useditem/usedItems";
    }

    //특정 아이템에 대한 조회 수행
    @GetMapping("/{usedItemId}")
    public String usedItem(@PathVariable Long usedItemId, Model model) {
        UsedItem usedItem = usedItemService.findById(usedItemId);
        log.info("usedItem: {}", usedItem);
        model.addAttribute("usedItem", usedItem);
        model.addAttribute("category", usedItem.getCategory());
        User user = getUserFromSecurityContext();
        log.info("usedItem: {},user: {} , equality: {}", usedItem, user,usedItem.getSeller().equals(user));
        //해당 상품을 등록한 사용자인경우 상품 관리창으로 이동
        if(usedItem.getSeller().getEmail().equals(user.getEmail()))
            return "useditem/usedItemSellerForm";
        else
            return "useditem/usedItemCustomerForm";
    }

    //아이템 등록 창을 넘겨준다.
    @GetMapping("/add")
    public String addForm(Model model) {
        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("usedItem", new UsedItem());

        return "useditem/addForm";
    }

    //상품을 등록하는 로직
    @PostMapping("/add")
    public String addUsedItem(@Validated @ModelAttribute("usedItem") UsedItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 아이템을 등록하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "useditem/addForm";
        }

        log.info("category:{}", form.getCategory());
        Long savedId = usedItemService.save(form,getUserFromSecurityContext());
        log.info("savedID: {}", savedId);
        redirectAttributes.addAttribute("usedItemId", savedId);

        return "redirect:/usedItems/{usedItemId}";
    }

    //기존에 등록된 상품에 대한 수정을 담당하는 로직
    @GetMapping("/{usedItemId}/edit")
    public String editForm(@PathVariable Long usedItemId, Model model,RedirectAttributes redirectAttributes) {
        //기존에 등록한 아이템을 찾아서 해당 정보를 수정 화면에 보여준다.
        UsedItem searchItem = usedItemService.findByIdWithUser(usedItemId);
        User user = getUserFromSecurityContext();

        //해당 상품의 등록자가 아닌 경우 인가 금지 처리
        if(!searchItem.getSeller().getEmail().equals(user.getEmail()))
            return "redirect:/denied?exception=Access Denied";


        //thymeleaf 기반의 th.object 활용을 위해 빈 객체를 생성해서 넘긴다.
        model.addAttribute("usedItem", searchItem);
        return "useditem/editForm";
    }

    //상품을 수정하는 로직
    @PostMapping("/{usedItemId}/edit")
    public String editUsedItem(@Validated @ModelAttribute("usedItem") UsedItemEditForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        //에러가 발생할 경우 이전 창으로 돌아게된다.해당 경우에는 아이템을 수정하는 창으로 넘어간다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "useditem/editForm";
        }
        User user = getUserFromSecurityContext();

        log.info("usedItem: {},user: {} , equality: {}",user,form.getSeller(),user.equals(form.getSeller()));
        //해당 상품의 등록자가 아닌 경우 인가 금지 처리
        if(!form.getSeller().getEmail().equals(user.getEmail()))
            return "redirect:/denied?exception=Access Denied";

        Long editedId = usedItemService.edit(form);
        redirectAttributes.addAttribute("usedItemId", editedId);

        return "redirect:/usedItems/{usedItemId}";
    }

    //아이템을 삭제하기 위한 로직
    @GetMapping("/{usedItemId}/delete")
    public String deleteUsedItem(@PathVariable Long usedItemId, Model model) {
        UsedItem usedItem = usedItemService.findById(usedItemId);
        User user = getUserFromSecurityContext();

        //해당 상품의 등록자가 아닌 경우 인가 금지 처리
        if(!usedItem.getSeller().getEmail().equals(user.getEmail()))
            return "redirect:/denied?exception=Access Denied";

        model.addAttribute("usedItem", usedItem);
        log.info("deletedItem:{}", usedItem);
        return "useditem/deleteForm";
    }

    @PostMapping("/{usedItemId}/delete")
    public String deleteUsedItem(@PathVariable Long usedItemId, @ModelAttribute("usedItem") UsedItemEditForm form,BindingResult bindingResult) {
        User user = getUserFromSecurityContext();

        //해당 상품의 등록자가 아닌 경우 인가 금지 처리
        if(!form.getSeller().getEmail().equals(user.getEmail()))
            return "redirect:/denied?exception=Access Denied";
        usedItemService.deleteById(form.getId());
        return "redirect:/usedItems";
    }

    private User getUserFromSecurityContext() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
