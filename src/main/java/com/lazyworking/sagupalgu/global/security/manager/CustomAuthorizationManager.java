package com.lazyworking.sagupalgu.global.security.manager;

import com.lazyworking.sagupalgu.global.security.service.SecurityResourceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private final SecurityResourceService securityResourceService;

    private List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> defaultMappings = new ArrayList<>();

    private List<RequestMatcher> permitAlls = new ArrayList<>();

    private List<RequestMatcher> deniedIps=new ArrayList<>();

    private Boolean authenticated;
    private final Log logger = LogFactory.getLog(getClass());

    public void setPermitAlls(String... urls) {
        for(String url: urls)
            this.permitAlls.add(new AntPathRequestMatcher(url));
    }

    public void setDeniedIps(List<RequestMatcher> deniedIps) {
        this.deniedIps = deniedIps;
    }

    public void setAuthenticated(Boolean authenticated) {
        if(authenticated)
            this.defaultMappings.add(new RequestMatcherEntry<>(new AntPathRequestMatcher("**"), AuthenticatedAuthorizationManager.authenticated()));
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        if (this.logger.isTraceEnabled())
            this.logger.trace(LogMessage.format("Authorizing %s", request));


        for (RequestMatcher ipMatcher : deniedIps) {
            if(ipMatcher.matches(request))
                return DENY;
        }

        for(RequestMatcher permitMatcher: permitAlls)
            if(permitMatcher.matches(request))
                return new AuthorizationDecision(true);

        List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings = securityResourceService.getUrlResourceList();
        mappings.addAll(defaultMappings);

        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : mappings) {
            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request);
            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace(LogMessage.format("Checking authorization on %s using %s", request, manager));
                }
                return manager.check(authentication,
                        new RequestAuthorizationContext(request, matchResult.getVariables()));
            }
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.of(() -> "Denying request since did not find matching RequestMatcher"));
        }
        return DENY;
    }
}
