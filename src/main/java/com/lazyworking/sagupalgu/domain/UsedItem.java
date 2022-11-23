package com.lazyworking.sagupalgu.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UsedItem {
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Convert(converter=BooleanToYNConverter.class)
    private boolean ifSelled;

    @Column(length = 40)
    private String context;
}
