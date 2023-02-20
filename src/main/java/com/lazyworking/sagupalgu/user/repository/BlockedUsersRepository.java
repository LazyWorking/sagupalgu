package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUsersRepository extends JpaRepository<BlockedUsers, Long> {
}
