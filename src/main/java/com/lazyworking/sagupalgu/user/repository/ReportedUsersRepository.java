package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportedUsersRepository extends JpaRepository<ReportedUsers, Long> {
    @Query("select new com.lazyworking.sagupalgu.user.domain.ReportedUserDTO(r.targetUser,count(*)) from ReportedUsers r group by r.targetUser")
    List<ReportedUserDTO> groupByUser();

    List<ReportedUsers> findByTargetUser(User targetUser);
}
