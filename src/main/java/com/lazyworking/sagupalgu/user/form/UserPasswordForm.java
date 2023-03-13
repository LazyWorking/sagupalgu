package com.lazyworking.sagupalgu.user.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class UserPasswordForm {

    private Long id;

    @NotBlank
    @Length(min=10,max=20)
    private String password;

    public UserPasswordForm(Long id) {
        this.id = id;
    }

    public UserPasswordForm(Long id, String password) {
        this.id = id;
        this.password = password;
    }
}
