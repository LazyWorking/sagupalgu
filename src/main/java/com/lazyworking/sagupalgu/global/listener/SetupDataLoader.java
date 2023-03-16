package com.lazyworking.sagupalgu.global.listener;

import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.resources.domain.RoleUser;
import com.lazyworking.sagupalgu.resources.repository.RoleRepository;
import com.lazyworking.sagupalgu.resources.repository.RoleUserRepository;
import com.lazyworking.sagupalgu.resources.service.RoleService;
import com.lazyworking.sagupalgu.resources.service.RoleUserService;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import com.lazyworking.sagupalgu.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Transactional
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private static AtomicInteger count = new AtomicInteger(0);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
        User user= new User("user","user@email.com",passwordEncoder.encode("1234"), LocalDateTime.now(), Gender.M);
        User admin= new User("admin","admin@email.com",passwordEncoder.encode("1234"), LocalDateTime.now(),Gender.F);
        userRepository.save(user);
        userRepository.save(admin);

        //초기 권한 설정
        Role roleUser = new Role("ROLE_USER", "유저");
        Role roleAdmin = new Role("ROLE_ADMIN", "관리자");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);

        //유저 권한 매핑
        RoleUser roleUser1= new RoleUser();
        roleUser1.setRoleUser(user, roleUser);

        RoleUser roleUser2= new RoleUser();
        roleUser2.setRoleUser(admin, roleAdmin);

        roleUserRepository.save(roleUser1);
        roleUserRepository.save(roleUser2);

    }
}
