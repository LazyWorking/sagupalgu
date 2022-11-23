package com.lazyworking.sagupalgu.repository;

import com.lazyworking.sagupalgu.domain.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsedItemRepository extends JpaRepository<UsedItem,Long> {
    List<UsedItem> findByNameContaining(String name);
}
