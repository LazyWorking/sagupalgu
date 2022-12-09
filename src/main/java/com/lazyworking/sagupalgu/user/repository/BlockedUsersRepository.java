package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BlockedUsersRepository extends JpaRepository<BlockedUsers, Long> {
}
