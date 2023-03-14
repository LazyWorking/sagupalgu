package com.lazyworking.sagupalgu.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

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
