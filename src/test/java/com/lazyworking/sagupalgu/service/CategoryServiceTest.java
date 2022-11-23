package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.domain.Category;
import com.lazyworking.sagupalgu.domain.UsedItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UsedItemService usedItemService;

    @Test
    @DisplayName("카테고리 등록")
    void save() {
        //given
        Category category = new Category();
        category.setName("category1");

        //when
        Category savedCategory = categoryService.save(category);

        //then
        assertThat(category).isEqualTo(savedCategory);
        log.info("category: {}", category);
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteById() {
        //given
        Category category = new Category();
        category.setName("category1");
        Category savedCategory = categoryService.save(category);

        //when
        categoryService.deleteById(savedCategory.getId());

        //then
        //이미 삭제한 카테고리에 대한 조회를 수행한 경우 NoSuchElementException 에러가 발생하게 된다.
        assertThatThrownBy(() -> {
            Category searchCategory = categoryService.findById(savedCategory.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("카테고리 조회 - id")
    void findById() {
        //given
        Category category = new Category();
        category.setName("category1");

        //when
        Category savedCategory = categoryService.save(category);
        Category searchedCategory = categoryService.findById(savedCategory.getId());

        //then
        assertThat(searchedCategory).isEqualTo(savedCategory);
        log.info("searchedCategory: {}", searchedCategory);
    }

    @Test
    @DisplayName("아이템 조회 - 이름")
    void findByName() {
        //given
        Category category1 = new Category();
        category1.setName("category1");

        Category category2 = new Category();
        category2.setName("category2");

        //when
        categoryService.save(category1);
        categoryService.save(category2);

        Category searchedCategory = categoryService.findByName("category1");

        //then
        assertThat(searchedCategory).isEqualTo(category1);
        log.info("searchedCategory: {}", searchedCategory);
    }

    @Test
    @DisplayName("아이템 목록 조회")
    void findAllUsedItems() {
        //given
        Category category1 = new Category();
        category1.setName("category1");

        Category category2 = new Category();
        category2.setName("category2");

        //when
        categoryService.save(category1);
        categoryService.save(category2);

        List<Category> categories = categoryService.findAllCategories();

        //then
        assertThat(categories.size()).isEqualTo(2);
        for (Category category : categories) {
            log.info("category: {}", category);
        }
    }

    @Test
    @DisplayName("카테고리 + 아이템 목록 조회")
    void findCategoriesAndItems() {
        //given
        Category category = new Category();
        category.setName("category1");

        UsedItem item1 = new UsedItem();
        item1.setName("item1");
        item1.setPrice(1234);
        item1.setContext("Item1 is ~~");
        item1.setIfSelled(false);
        item1.setCategory(category);

        UsedItem item2 = new UsedItem();
        item2.setName("item2");
        item2.setPrice(12345);
        item2.setContext("Item2 is ~~");
        item2.setIfSelled(true);
        item2.setCategory(category);

        categoryService.save(category);

        //when
        Category searchedCategory = categoryService.findById(category.getId());
        List<UsedItem> items = searchedCategory.getItemList();
        //then
        assertThat(items.size()).isEqualTo(2);

        log.info("category: {}", category);
        for (UsedItem item : items) {
            log.info("item: {}", item);
        }



    }
}