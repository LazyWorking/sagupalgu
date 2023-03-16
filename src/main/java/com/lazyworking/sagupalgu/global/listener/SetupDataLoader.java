package com.lazyworking.sagupalgu.global.listener;

import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.resources.domain.RoleHierarchy;
import com.lazyworking.sagupalgu.resources.domain.RoleUser;
import com.lazyworking.sagupalgu.resources.repository.RoleHierarchyRepository;
import com.lazyworking.sagupalgu.resources.repository.RoleRepository;
import com.lazyworking.sagupalgu.resources.repository.RoleUserRepository;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Transactional
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private static AtomicInteger count = new AtomicInteger(0);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;
    private final RoleUserRepository roleUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupData();

        alreadySetup = true;
    }

    private void setupData() {

        //초기 유저 생성
        User user= createUser(new User("user","user@email.com",passwordEncoder.encode("1234"), LocalDateTime.now(), Gender.M));
        User manager=createUser(new User("manager","manager@email.com",passwordEncoder.encode("1234"), LocalDateTime.now(),Gender.F));
        User admin=createUser(new User("admin","admin@email.com",passwordEncoder.encode("1234"), LocalDateTime.now(),Gender.F));

        //초기 권한 설정
        Role roleUser = createRole(new Role("ROLE_USER", "유저"));
        Role roleManager = createRole(new Role("ROLE_MANAGER", "매니저"));
        Role roleAdmin = createRole(new Role("ROLE_ADMIN", "관리자"));

        //유저 권한 매핑
        createRoleUser(user,roleUser);
        createRoleUser(admin,roleAdmin);
        createRoleUser(manager, roleManager);

        //권한 계층 관계 설정
        createRoleHierarchy(roleUser,roleManagerㅋ   );
        createRoleHierarchy(roleManager,roleAdmin);
    }
    private User createUser(User user) {
        userRepository.save(user);
        return user;
    }
    private Role createRole(Role role) {
        roleRepository.save(role);
        return role;
    }

    private void createRoleUser(User user, Role role) {
        RoleUser roleUser= new RoleUser();
        roleUser.setRoleUser(user, role);
        roleUserRepository.save(roleUser);
    }

    private void createRoleHierarchy(Role childRole, Role parentRole) {
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.findByChildRole(parentRole.getRoleName());
        if (parentRoleHierarchy == null) {
            parentRoleHierarchy = new RoleHierarchy();
            parentRoleHierarchy.setChildRole(parentRole.getRoleName());
        }
        roleHierarchyRepository.save(parentRoleHierarchy);

        RoleHierarchy childRoleHierarchy= roleHierarchyRepository.findByChildRole(childRole.getRoleName());
        if (childRoleHierarchy == null) {
            childRoleHierarchy = new RoleHierarchy();
            childRoleHierarchy.setChildRole(childRole.getRoleName());
        }
        childRoleHierarchy.setParentRole(parentRoleHierarchy);
        roleHierarchyRepository.save(childRoleHierarchy);
    }

}