package com.lazyworking.sagupalgu.login.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

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
