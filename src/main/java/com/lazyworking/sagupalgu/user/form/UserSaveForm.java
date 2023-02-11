package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.user.domain.Gender;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class UserSaveForm {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
