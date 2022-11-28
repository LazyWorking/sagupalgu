package com.lazyworking.sagupalgu.security;

import com.lazyworking.sagupalgu.exception.AccessTokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        String accessToken = jwtTokenProvider.resolveToken((HttpServletRequest) request, "AccessToken");

        if(accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else if(accessToken != null && !jwtTokenProvider.validateToken(accessToken)){
            throw new AccessTokenExpiredException();
        }

        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            log.error("JwtAuthenticationFilter: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
