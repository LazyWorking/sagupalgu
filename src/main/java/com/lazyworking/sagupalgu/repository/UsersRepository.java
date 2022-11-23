package com.lazyworking.sagupalgu.repository;

import com.lazyworking.sagupalgu.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String userId);
}
