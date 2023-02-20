package com.lazyworking.sagupalgu.user.domain;

import lombok.Data;

/*
 * author: JehyunJung
 * purpose: domain for reportedUser
 * version: 1.0
 */
@Data
public class ReportedUserDTO {
    private User user;
    private Long count;

    public ReportedUserDTO(User user, Long count) {
        this.user = user;
        this.count = count;
    }
}

