package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

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
