package com.lazyworking.sagupalgu.resources.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Long id;

    private String roleName;

    private String roleDesc;

    public Role(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }
}
