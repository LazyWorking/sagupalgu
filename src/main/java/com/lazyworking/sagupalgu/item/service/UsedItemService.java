package com.lazyworking.sagupalgu.item.service;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.category.repository.CategoryRepository;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.form.UsedItemEditForm;
import com.lazyworking.sagupalgu.item.form.UsedItemSaveForm;
import com.lazyworking.sagupalgu.item.repository.UsedItemRepository;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
/*
 * author: JehyunJung
 * purpose: service for UsedItem
 * version: 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    @Transactional
    public Long save(UsedItem item) {
        UsedItem saveItem = usedItemRepository.save(item);
        return saveItem.getId();
    }

    @Transactional
    public Long save(UsedItemSaveForm form,User seller) {

        UsedItem usedItem = new UsedItem();
        usedItem.setName(form.getName());
        usedItem.setPrice(form.getPrice());
        usedItem.setContext(form.getContext());

        Category category = categoryRepository.findById(form.getCategory().getId()).get();
        usedItem.setCategory(category);

        User user = userRepository.findById(seller.getId()).get();
        usedItem.setSeller(user);

        UsedItem saveItem = usedItemRepository.save(usedItem);
        return saveItem.getId();
    }
    @Transactional
    public Long edit(UsedItemEditForm form){
        UsedItem usedItem = findById(form.getId());

        usedItem.setName(form.getName());
        usedItem.setPrice(form.getPrice());
        usedItem.setContext(form.getContext());
        usedItem.setIfSelled(form.isIfSelled());

        //기존의 연관관계 끊고 새로 매핑 실시
        if(!form.getCategory().equals(usedItem.getCategory())) {
            Category category = categoryRepository.findById(form.getCategory().getId()).get();
            category.getItemList().remove(usedItem);
            usedItem.setCategory(category);
        }

        return usedItem.getId();
    }

    public List<UsedItem> findByName(String name) {
        return usedItemRepository.findByNameContaining(name);
    }

    public List<UsedItem> findAllUsedItems() {
        return usedItemRepository.findAll();
    }

    @Transactional
    public void deleteById(long id) {
        UsedItem usedItem = usedItemRepository.findById(id).get();

        Category category = categoryRepository.findById(usedItem.getCategory().getId()).get();
        User user = userRepository.findById(usedItem.getSeller().getId()).get();

        category.getItemList().remove(usedItem);
        user.getUsedItems().remove(usedItem);

        usedItemRepository.deleteById(id);
    }

    public UsedItem findById(long id) {
        return usedItemRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public UsedItem findByIdWithUser(Long id) {
        return usedItemRepository.findByIdWithUser(id);
    }

    public List<UsedItem> getUsedItemsBySeller(User user){
        return usedItemRepository.findBySeller(user);
    }

}
