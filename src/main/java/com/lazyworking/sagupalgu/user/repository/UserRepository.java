package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public List<User> findByEmail(String email);
}
