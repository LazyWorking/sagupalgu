package com.lazyworking.sagupalgu.repository;

import com.lazyworking.sagupalgu.domain.Category;
import com.lazyworking.sagupalgu.domain.UsedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
