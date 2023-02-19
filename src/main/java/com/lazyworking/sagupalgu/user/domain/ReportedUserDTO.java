package com.lazyworking.sagupalgu.user.domain;

/*
 * author: JehyunJung
 * purpose: domain for reportedUser
 * version: 1.0
 */
public class ReportedUserDTO {
    private User user;
    private Long count;

    public ReportedUserDTO(User user, Long count) {
        this.user = user;
        this.count = count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

