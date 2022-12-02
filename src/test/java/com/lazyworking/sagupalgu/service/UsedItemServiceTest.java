package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.service.UsedItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class UsedItemServiceTest {
    @Autowired
    private UsedItemService usedItemService;

    @Test
    @DisplayName("아이템 등록")
    void save() {
        //given
        UsedItem item = new UsedItem();
        item.setName("item1");
        item.setPrice(1234);
        item.setContext("Item1 is ~~");
        item.setIfSelled(false);
        //when
        UsedItem savedItem = usedItemService.save(item);

        //then
        assertThat(item).isEqualTo(savedItem);
        log.info("savedItem: {}", savedItem);
    }

    @Test
    @DisplayName("아이템 삭제")
    void deleteById() {
        //given
        UsedItem item = new UsedItem();
        item.setName("item1");
        item.setPrice(1234);
        item.setContext("Item1 is ~~");
        item.setIfSelled(false);
        UsedItem savedItem = usedItemService.save(item);

        //when
        usedItemService.deleteById(savedItem.getId());

        //then
        //이미 삭제한 아이템에 대한 조회를 수행한 경우 NoSuchElementException 에러가 발생하게 된다.
        assertThatThrownBy(() -> {
            UsedItem searchItem = usedItemService.findById(savedItem.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("아이템 조회 - id")
    void findById() {
        //given
        UsedItem item = new UsedItem();
        item.setName("item1");
        item.setPrice(1234);
        item.setContext("Item1 is ~~");
        item.setIfSelled(false);
        //when
        UsedItem savedItem = usedItemService.save(item);
        UsedItem searchItem = usedItemService.findById(savedItem.getId());

        //then
        assertThat(searchItem).isEqualTo(savedItem);
        log.info("searchItem: {}", searchItem);
    }

    @Test
    @DisplayName("아이템 조회 - 이름")
    void findByName() {
        //given
        UsedItem item1 = new UsedItem();
        item1.setName("item1");
        item1.setPrice(1234);
        item1.setContext("Item1 is ~~");
        item1.setIfSelled(false);

        UsedItem item2 = new UsedItem();
        item2.setName("item2");
        item2.setPrice(12345);
        item2.setContext("Item2 is ~~");
        item2.setIfSelled(true);

        UsedItem item3 = new UsedItem();
        item3.setName("i3");
        item3.setPrice(12345);
        item3.setContext("i3 is ~~");
        item3.setIfSelled(true);

        //when
        usedItemService.save(item1);
        usedItemService.save(item2);
        usedItemService.save(item3);

        List<UsedItem> items = usedItemService.findByName("item");

        //then
        assertThat(items.size()).isEqualTo(2);

        for (UsedItem item : items) {
            log.info("item: {}", item);
        }
    }

    @Test
    @DisplayName("아이템 목록 조회")
    void findAllUsedItems() {
        //given
        UsedItem item1 = new UsedItem();
        item1.setName("item1");
        item1.setPrice(1234);
        item1.setContext("Item1 is ~~");
        item1.setIfSelled(false);

        UsedItem item2 = new UsedItem();
        item2.setName("item2");
        item2.setPrice(12345);
        item2.setContext("Item2 is ~~");
        item2.setIfSelled(true);

        UsedItem item3 = new UsedItem();
        item3.setName("i3");
        item3.setPrice(12345);
        item3.setContext("i3 is ~~");
        item3.setIfSelled(true);

        //when
        usedItemService.save(item1);
        usedItemService.save(item2);
        usedItemService.save(item3);

        List<UsedItem> items = usedItemService.findAllUsedItems();

        //then
        assertThat(items.size()).isEqualTo(3);

        for (UsedItem item : items) {
            log.info("item: {}", item);
        }
    }
}