package com.lazyworking.sagupalgu.admin.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleForm {
    @NotNull
    private Long id;
    @NotBlank
    private String roleName;
    @NotBlank
    private String roleDesc;
}
