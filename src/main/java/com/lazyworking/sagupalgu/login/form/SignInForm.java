package com.lazyworking.sagupalgu.login.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SignInForm {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    //@Length(min=10,max=20)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public SignInForm(String name, String email, String password, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }

    public User toEntity() {
        return new User(this.name, this.email, this.password, LocalDateTime.now(),this.gender);
    }

}
