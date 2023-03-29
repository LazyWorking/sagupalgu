package com.lazyworking.sagupalgu.admin.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleAddForm {
    @NotBlank
    private String roleName;
    @NotBlank
    private String roleDesc;
}
