package com.lazyworking.sagupalgu.global.security.service;

import com.lazyworking.sagupalgu.resources.domain.Resource;
import com.lazyworking.sagupalgu.resources.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityResourceService {
    private final ResourceRepository resourceRepository;
    private final RoleHierarchyImpl roleHierarchyImpl;
    List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> urlResources=new ArrayList<>();

    public void loadUrlResources() {
        urlResources.clear();

        List<Resource> allResources = resourceRepository.findAllUrlResources();

        allResources.forEach(
                (resource)->{
                    AuthorityAuthorizationManager<RequestAuthorizationContext> authorizationManager = AuthorityAuthorizationManager.hasAnyAuthority(
                            resource.getResourceRoles()
                                    .stream()
                                    .map(resourceRole -> resourceRole.getRole().getRoleName())
                                    .toArray(String[]::new));
                    authorizationManager.setRoleHierarchy(roleHierarchyImpl);
                    urlResources.add(new RequestMatcherEntry<>(new AntPathRequestMatcher(resource.getResourceName()),authorizationManager));
                }
        );
    }
    public List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> getUrlResourceList() {
        return urlResources;
    }

}
