package com.lazyworking.sagupalgu.global.security.config;

import com.lazyworking.sagupalgu.global.security.filter.CustomAuthorizationFilter;
import com.lazyworking.sagupalgu.global.security.handler.FormAccessDeniedHandler;
import com.lazyworking.sagupalgu.global.security.handler.FormAuthenticationFailureHandler;
import com.lazyworking.sagupalgu.global.security.handler.FormAuthenticationSuccessHandler;
import com.lazyworking.sagupalgu.global.security.manager.CustomAuthorizationManager;
import com.lazyworking.sagupalgu.global.security.provider.FormAuthenticationProvider;
import com.lazyworking.sagupalgu.global.security.webdetails.FormAuthenticationDetailsSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {
    //인증 성공 핸들러
    private final FormAuthenticationSuccessHandler formAuthenticationSuccessHandler;
    //인증 실패 핸들러
    private final FormAuthenticationFailureHandler formAuthenticationFailureHandler;
    //인간 실패 핸들러
    private final FormAccessDeniedHandler formAccessDeniedHandler;
    //로그인시 추가되는 사용자 관련 정보
    private final FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    //인증을 수행하는 authentication provider
    private final FormAuthenticationProvider formAuthenticationProvider;

    //계층관계 정보를 가지는 Spring 내부 객체
    @Bean
    public RoleHierarchyImpl roleHierarchyImpl(){
        return new RoleHierarchyImpl();
    }

    //authentication provider을 호출하는 authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        ProviderManager providerManager = (ProviderManager) authenticationConfiguration.getAuthenticationManager();
        providerManager.getProviders().add(formAuthenticationProvider);
        return providerManager;
    }

    //인가 검증을 수행하는 CustomAuthorizationManager
    public CustomAuthorizationManager customAuthorizationManager() {
        CustomAuthorizationManager customAuthorizationManager = new CustomAuthorizationManager();
        //모든 접근을 허용하고자할 url 지정
        customAuthorizationManager.setPermitAlls("/","/home","/denied**","/error**","/signIn","/login**");
        //나머지 url 자원에 대해서는 인증을 필수적으로 진행하게끔 설정
        customAuthorizationManager.setAuthenticated(true);
        return customAuthorizationManager;
    }

    //authorization manager을 호출하는 authorization filter
    public CustomAuthorizationFilter customAuthorizationFilter(){
        return new CustomAuthorizationFilter(customAuthorizationManager());
    }


    //정적 자원에 대한 보안 필터링 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //기존의 보안 필터는 모두 통과하도록 설정
        httpSecurity
                .authorizeHttpRequests()
                .anyRequest().permitAll();
        //모든 인가 검증은 customauthorizationfilter에서 처리된다.
        httpSecurity.addFilterAfter( customAuthorizationFilter(), AuthorizationFilter.class);

        //로그인 관련 설정
        httpSecurity.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .successHandler(formAuthenticationSuccessHandler)
                .failureHandler(formAuthenticationFailureHandler)
                .permitAll();

        //spring security에서 발생하는 예외 처리 관련 설정
        formAccessDeniedHandler.setErrorPage("/denied");
        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/denied")
                .accessDeniedHandler(formAccessDeniedHandler);

        return httpSecurity.build();

    }
}
