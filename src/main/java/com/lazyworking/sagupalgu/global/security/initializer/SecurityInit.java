package com.lazyworking.sagupalgu.global.security.initializer;

import com.lazyworking.sagupalgu.resources.service.RoleHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityInit implements ApplicationRunner {
    private final RoleHierarchyService roleHierarchyService;
    private final RoleHierarchyImpl roleHierarchy;
    
    //Spring Security가 가동할때, 롤계층 관련 정보를 RoleHierarchy에 전달
    @Override
    public void run(ApplicationArguments args) throws Exception {
        roleHierarchy.setHierarchy(roleHierarchyService.findAllHierarchy());
    }
}
