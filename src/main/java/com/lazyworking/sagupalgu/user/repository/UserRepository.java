package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
