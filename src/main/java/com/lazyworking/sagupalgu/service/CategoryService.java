package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.domain.Category;
import com.lazyworking.sagupalgu.domain.UsedItem;
import com.lazyworking.sagupalgu.repository.CategoryRepository;
import com.lazyworking.sagupalgu.repository.UsedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(long id) {
        categoryRepository.deleteById(id);
    }

    public Category findById(long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}
