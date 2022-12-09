package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReportedUsersService {
    private final ReportedUsersRepository reportedUsersRepository;

    //유저 별로 grouping 된 신고 목록을 조회한다.
    public List<ReportedUserDTO> getReportedUsers() {
        return reportedUsersRepository.groupByUser();
    }

    //해당 유저에 대한 상세 신고 항목을 보여준다.
    public List<ReportedUsers> getReportedUser(Users user) {
        return reportedUsersRepository.findByUser(user);
    }

}
