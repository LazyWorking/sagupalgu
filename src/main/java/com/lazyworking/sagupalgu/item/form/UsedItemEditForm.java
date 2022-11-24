package com.lazyworking.sagupalgu.item.form;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.global.converter.BooleanToYNConverter;
import lombok.Data;

import javax.persistence.*;

/*
 * author: JehyunJung
 * purpose: editForm for UsedItem
 * version: 1.0
 */
@Data
public class UsedItemSaveForm {
    private String name;

}

public class UsedItem {
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean ifSelled;

    @Column(length = 40)
    private String context;

    @ManyToOne
    @JoinColumn
    private Category category;

    //양방향 연결관계 설정
    public void setCategory(Category category) {
        if(!category.getItemList().contains(this))
            category.getItemList().add(this);
        this.category = category;
    }

    @Override
    public String toString(){
        return String.format("[id:%d\tname:%s\tprice:%d\tifSelled:%s\tContext:%s\tCategory:%s]", id, name, price, ifSelled, context, category);
    }