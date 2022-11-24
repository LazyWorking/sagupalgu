package com.lazyworking.sagupalgu.item.service;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.repository.UsedItemRepository;
import lombok.RequiredArgsConstructor;
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
@Transactional
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;

    public UsedItem save(UsedItem item) {
        UsedItem saveItem = usedItemRepository.save(item);
        return saveItem;
    }

    public List<UsedItem> findByName(String name) {
        return usedItemRepository.findByNameContaining(name);
    }

    public List<UsedItem> findAllUsedItems() {
        return usedItemRepository.findAll();
    }

    public void deleteById(long id) {
        usedItemRepository.deleteById(id);
    }

    public UsedItem findById(long id) {
        return usedItemRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

}
