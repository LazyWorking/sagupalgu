package com.lazyworking.sagupalgu.admin.form;

import com.lazyworking.sagupalgu.admin.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceForm {
    @NotNull
    private Long id;
    @NotNull
    private int orderNum;
    @NotBlank
    private String resourceName;

    private String httpMethod;
    @NotBlank
    private String resourceType;
    private List<Role> roles;
}
