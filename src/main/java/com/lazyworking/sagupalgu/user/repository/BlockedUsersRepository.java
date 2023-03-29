package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUsersRepository extends JpaRepository<BlockedUsers, Long> {
    BlockedUsers findByUser(User user);
}
