package com.lazyworking.sagupalgu.user.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/*
 * author: JehyunJung
 * purpose: domain for reportedUser
 * version: 1.0
 */
@Data
@Entity
@NoArgsConstructor
public class ReportedUsers {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private User reporter;

    @ManyToOne
    @JoinColumn
    private User targetUser;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    @Column(length = 255)
    private String context;

    public ReportedUsers(User reporter, User targetUser, String context) {
        this.reporter = reporter;
        this.targetUser = targetUser;
        this.date = LocalDateTime.now();
        this.context = context;
    }

}

