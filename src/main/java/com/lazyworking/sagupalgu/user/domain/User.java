package com.lazyworking.sagupalgu.user.domain;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.global.converter.BooleanToYNConverter;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.user.converter.GenderConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    @Override
    public String toString() {
        return String.format("[id:%d\tname:%s\temail:%s\tpassword:%s\tjoinDate:%tF\tgender:%s]", id, name, email, password, joinDate, gender.getValue());
    }

    //Dto 변환
}
