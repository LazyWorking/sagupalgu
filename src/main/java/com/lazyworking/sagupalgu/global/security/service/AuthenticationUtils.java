package com.lazyworking.sagupalgu.global.security.service;

import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

    private AuthenticationUtils() throws InstantiationException {
        throw new InstantiationException();
    }

    public static User getUserFromSecurityContext(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
