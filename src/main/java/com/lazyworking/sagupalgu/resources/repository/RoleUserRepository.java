package com.lazyworking.sagupalgu.resources.repository;

import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.resources.domain.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
}
