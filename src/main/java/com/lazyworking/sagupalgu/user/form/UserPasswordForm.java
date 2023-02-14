package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
