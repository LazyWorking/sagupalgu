package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserEditForm {
    private Long id;

    private String email;

    @NotBlank
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserEditForm(Long id, String name, String email,String password,Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
