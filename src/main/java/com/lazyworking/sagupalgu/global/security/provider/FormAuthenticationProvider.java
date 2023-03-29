package com.lazyworking.sagupalgu.global.security.provider;

import com.lazyworking.sagupalgu.global.security.service.AccountContext;
import com.lazyworking.sagupalgu.global.security.webdetails.FormWebAuthenticationDetails;
import com.lazyworking.sagupalgu.user.exception.UserLockedException;
import com.lazyworking.sagupalgu.user.service.BlockedUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String passWord = (String)authentication.getCredentials();

        AccountContext userDetails = (AccountContext)userDetailsService.loadUserByUsername(loginId);
        log.info("user: {}", userDetails);
        if(userDetails == null|| !passwordEncoder.matches(passWord,userDetails.getPassword())){
            throw new BadCredentialsException("BadCredentialException");
        }
        if (userDetails.getUser().getLocked()) {
            throw new UserLockedException("Blocked User");
        }

        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails)authentication.getDetails();
        String secretKey = details.getSecretKey();

        if(secretKey == null || !secretKey.equals("secret")){
            throw new InsufficientAuthenticationException("secret key error");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUser(), null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
