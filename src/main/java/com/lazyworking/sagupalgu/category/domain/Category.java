package com.lazyworking.sagupalgu.category.domain;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/*
 * author: JehyunJung
 * purpose: Entity Class for Category
 * version: 1.0
 */
@Entity
@Data
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UsedItem> itemList = new ArrayList<>();

    @Override
    public String toString(){
        return String.format("[id:%d\tname:%s]", id, name);
    }
}
