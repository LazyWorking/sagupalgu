package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    @Query("select u from User u join fetch u.roleUsers ru join fetch ru.role ")
    List<User> findAllUsersWithRoles();
}
