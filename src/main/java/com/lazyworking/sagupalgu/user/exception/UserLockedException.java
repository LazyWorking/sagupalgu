package com.lazyworking.sagupalgu.user.exception;


import org.springframework.security.core.AuthenticationException;

public class UserLockedException extends AuthenticationException {
    public UserLockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserLockedException(String msg) {
        super(msg);
    }
}
