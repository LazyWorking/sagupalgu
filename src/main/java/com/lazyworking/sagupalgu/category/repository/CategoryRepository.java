package com.lazyworking.sagupalgu.category.repository;

import com.lazyworking.sagupalgu.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * author: JehyunJung
 * purpose: repository for Category
 * version: 1.0
 */public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
