package com.lazyworking.sagupalgu.user.domain;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.global.converter.BooleanToYNConverter;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDateTime joinDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany
    private List<UsedItem> usedItems;

    public static User createUser(String name, String email, String password, LocalDateTime joinDate, Gender gender) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        user.joinDate = joinDate;
        user.gender = gender;
        return user;
    }

    //각종 변경 로직
    public void change(String name) {
        this.name = name;
    }
}
