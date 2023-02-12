package com.lazyworking.sagupalgu.user.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLoginForm {
    @NotNull
    private String email;

    @NotNull
    private String password;

    public UserLoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
