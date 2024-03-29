package com.lazyworking.sagupalgu.admin.repository;

import com.lazyworking.sagupalgu.admin.domain.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchy,Long> {
    //자식 계층을 기준으로 롤 계층 객체를 찾는다.
    RoleHierarchy findByChildRole(String roleName);
}
