package com.lazyworking.sagupalgu.user.domain;

import com.lazyworking.sagupalgu.admin.domain.RoleUser;
import com.lazyworking.sagupalgu.item.domain.UsedItem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
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

    @OneToMany(mappedBy="seller",fetch = FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval = true)
    private List<UsedItem> usedItems=new ArrayList<>();

    @OneToMany(mappedBy="user")
    private List<RoleUser> roleUsers=new ArrayList<>();

    public User (String name, String email, String password, LocalDateTime joinDate, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
        this.gender = gender;
    }

    //각종 변경 로직
    public void change(String name,Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeEverything(String name, String email, String password, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }


    @Override
    public String toString() {
        return String.format("[id:%d\tname:%s\temail:%s\tpassword:%s\tjoinDate:%tF\tgender:%s]", id, name, email, password, joinDate, gender.getValue());
    }

    //Dto 변환
}
