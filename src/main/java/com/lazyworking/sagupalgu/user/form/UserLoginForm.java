package com.lazyworking.sagupalgu.user.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserLoginForm {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public UserLoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
