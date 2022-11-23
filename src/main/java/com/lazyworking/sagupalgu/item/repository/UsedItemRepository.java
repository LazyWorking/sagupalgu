package com.lazyworking.sagupalgu.item.repository;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsedItemRepository extends JpaRepository<UsedItem,Long> {
    List<UsedItem> findByNameContaining(String name);
}
