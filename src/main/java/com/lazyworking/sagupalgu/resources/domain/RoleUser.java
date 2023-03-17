package com.lazyworking.sagupalgu.resources.domain;

import com.lazyworking.sagupalgu.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUser {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    //연관관계 설정
    public void setRoleUser(User user, Role role) {
        this.user = user;
        this.role = role;
        if(!user.getRoleUsers().contains(this))
            user.getRoleUsers().add(this);
    }
}
