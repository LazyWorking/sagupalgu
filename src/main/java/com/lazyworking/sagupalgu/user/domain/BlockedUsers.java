package com.lazyworking.sagupalgu.user.domain;

import lombok.Data;
import org.apache.catalina.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/*
 * author: JehyunJung
 * purpose: domain for reportedUsers
 * version: 1.0
 */
@Data
@Entity
public class BlockedUsers {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn
    private Users user;
}

