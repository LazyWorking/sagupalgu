package com.lazyworking.sagupalgu.category.repository;

import com.lazyworking.sagupalgu.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
}
