package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.user.domain.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserAllDataForm {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime joinDate;

    private String email;

    @NotBlank
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private List<Role> roles;

    public UserAllDataForm(Long id,LocalDateTime joinDate ,String name, String email, String password, Gender gender, List<Role> roles) {
        this.id = id;
        this.joinDate = joinDate;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.roles = roles;
    }
}
