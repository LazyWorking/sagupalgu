package com.lazyworking.sagupalgu.resources.repository;

import com.lazyworking.sagupalgu.resources.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //Role의 이름을 기반으로 탐색을 수행하는 메소드
    Role findByRoleName(String name);
}
