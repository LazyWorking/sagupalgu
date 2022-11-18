package com.lazyworking.sagupalgu.db;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class TestEntity {
    @Id
    private long id;

    private String name;

    private int age;

    @Override
    public String toString() {
        return String.format("[id: %d\tname: %s\tage:%d]",id, name, age);
    }


}
