package com.lazyworking.sagupalgu.category.service;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.category.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/*
 * author: JehyunJung
 * purpose: service class for Category
 * version: 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {



    private final CategoryRepository categoryRepository;
    @PostConstruct
    void createSampleDatas(){
        Category category1=new Category();
        category1.setName("category1");
        Category category2=new Category();
        category2.setName("category2");
        Category category3=new Category();
        category3.setName("category3");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
    }

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
