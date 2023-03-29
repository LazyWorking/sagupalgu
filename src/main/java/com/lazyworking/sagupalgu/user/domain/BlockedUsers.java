package com.lazyworking.sagupalgu.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/*
 * author: JehyunJung
 * purpose: domain for reportedUsers
 * version: 1.0
 */
@Data
@Entity
@NoArgsConstructor
public class BlockedUsers {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn
    private User user;

    public BlockedUsers(User user) {
        this.date = LocalDateTime.now();
        this.user = user;
    }

}

