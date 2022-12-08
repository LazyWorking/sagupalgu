package com.lazyworking.sagupalgu.user.repository;

import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ReportedUsersRepository extends JpaRepository<ReportedUsers, Long> {
    @Query("select new com.lazyworking.sagupalgu.user.domain.ReportedUserDTO(r.user,count(*)) from ReportedUsers r group by r.user")
    List<ReportedUserDTO> groupByUser();

    List<ReportedUsers> findByUser(Users user);
}
