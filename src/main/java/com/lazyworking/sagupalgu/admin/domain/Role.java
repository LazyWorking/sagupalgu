package com.lazyworking.sagupalgu.admin.domain;

import com.lazyworking.sagupalgu.admin.form.RoleForm;
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
    //권한 이름
    private String roleName;
    //권한 상세
    private String roleDesc;
    public Role(String roleName, String roleDesc) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }

    public void change(RoleForm roleForm) {
        this.roleName = roleForm.getRoleName();
        this.roleDesc = roleForm.getRoleDesc();

    }
}
