package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.domain.UsedItem;
import com.lazyworking.sagupalgu.repository.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class UsedItemService {
    private final UsedItemRepository usedItemRepository;

    public void save(UsedItem item) {
        usedItemRepository.save(item);
    }

    public void deleteById(long id) {
        usedItemRepository.deleteById(id);
    }

    public UsedItem findById(long id) {
        return usedItemRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public List<UsedItem> findByName(String name) {
        return usedItemRepository.findByNameContaining(name);
    }

    public List<UsedItem> findAllUsedItems() {
        return usedItemRepository.findAll();
    }
}
