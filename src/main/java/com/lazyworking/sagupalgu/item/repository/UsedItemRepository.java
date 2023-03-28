package com.lazyworking.sagupalgu.item.repository;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * author: JehyunJung
 * purpose: repository for UsedItem
 * version: 1.0
 */
public interface UsedItemRepository extends JpaRepository<UsedItem,Long> {
    List<UsedItem> findByNameContaining(String name);

    List<UsedItem> findBySeller(User seller);

    @Query("select ui from UsedItem ui join fetch ui.seller where ui.id = :id")
    UsedItem findByIdWithUser(@Param("id") Long id);
}
