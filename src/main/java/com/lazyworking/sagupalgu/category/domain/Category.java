package com.lazyworking.sagupalgu.category.domain;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import lombok.Data;

import javax.persistence.*;
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
    private long id;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UsedItem> itemList = new ArrayList<>();

    @Override
    public String toString(){
        return String.format("[id:%d\tname:%s]", id, name);
    }
}
