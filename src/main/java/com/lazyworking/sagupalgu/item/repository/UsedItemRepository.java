package com.lazyworking.sagupalgu.item.repository;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * author: JehyunJung
 * purpose: repository for UsedItem
 * version: 1.0
 */
public interface UsedItemRepository extends JpaRepository<UsedItem,Long> {
    List<UsedItem> findByNameContaining(String name);
}
