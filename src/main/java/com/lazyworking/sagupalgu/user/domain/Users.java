package com.lazyworking.sagupalgu.user.domain;

import com.lazyworking.sagupalgu.refreshToken.domain.RefreshToken;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(name="users")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    @NotNull
    private String email;

    @Column(length = 200)
    private String password;

    @Column(length = 10)
    private String name;

    @Column
    private Date joinDate;

    @Column(length = 1)
    @NotNull
    private String joinType;

    @Column(length = 1)
    @NotNull
    private String gender;

    @Column(length = 100, unique = true)
    @NotNull
    private String oauthId;

    @OneToOne
    @JoinColumn(name="id")
    private RefreshToken refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReportedUsers> reportedUsersList = new ArrayList<>();

    @OneToMany(mappedBy = "reporter")
    private List<ReportedUsers> reports = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
