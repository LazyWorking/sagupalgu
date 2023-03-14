package com.lazyworking.sagupalgu.global.security.config;

import com.lazyworking.sagupalgu.global.security.handler.FormAccessDeniedHandler;
import com.lazyworking.sagupalgu.global.security.handler.FormAuthenticationFailureHandler;
import com.lazyworking.sagupalgu.global.security.handler.FormAuthenticationSuccessHandler;
import com.lazyworking.sagupalgu.global.security.provider.FormAuthenticationProvider;
import com.lazyworking.sagupalgu.global.security.webdetails.FormAuthenticationDetailsSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {
    private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;

    private final FormAccessDeniedHandler formAccessDeniedHandler;

    private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;

    private final FormAuthenticationProvider formAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager providerManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        providerManager.getProviders().add(formAuthenticationProvider);
        return providerManager;
    }

    //정적 자원에 대한 보안 필터링 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/","/home","/denied**","/error**","/signIn","/usedItems","/login**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .permitAll();

        formAccessDeniedHandler.setErrorPage("/denied");

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/denied")
                .accessDeniedHandler(formAccessDeniedHandler);
        return httpSecurity.build();
    }
}
