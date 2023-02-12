package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserSaveForm {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    @Length(min=10,max=20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserSaveForm(String name, String email, String password, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public User toEntity() {
        return new User(this.name, this.email, this.password, LocalDateTime.now(),this.gender);
    }

}
