package com.lazyworking.sagupalgu.user.domain;

import com.lazyworking.sagupalgu.refreshToken.domain.RefreshToken;
import com.sun.istack.NotNull;
import lombok.*;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/*
 * author: JehyunJung
 * purpose: domain for reportedUsers
 * version: 1.0
 */
@Data
@Entity
public class ReportedUsers {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn
    private Users user;

    @ManyToOne
    @JoinColumn
    private Users reporter;

    @Column
    private LocalDateTime date;

    @Column(length = 100)
    private String context;
}

