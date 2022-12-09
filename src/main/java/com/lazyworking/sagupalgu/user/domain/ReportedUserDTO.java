package com.lazyworking.sagupalgu.user.domain;

/*
 * author: JehyunJung
 * purpose: domain for reportedUsers
 * version: 1.0
 */
public class ReportedUserDTO {
    private Users user;
    private Long count;

    public ReportedUserDTO(Users user, Long count) {
        this.user = user;
        this.count = count;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

