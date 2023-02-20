package com.lazyworking.sagupalgu.login.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginForm {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
