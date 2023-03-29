package com.lazyworking.sagupalgu.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(0)
public class CommonConfig {
    //암호화를 수행하는 객체
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //계층관계 정보를 가지는 Spring 내부 객체
    @Bean
    public RoleHierarchyImpl roleHierarchyImpl(){
        return new RoleHierarchyImpl();
    }


}
