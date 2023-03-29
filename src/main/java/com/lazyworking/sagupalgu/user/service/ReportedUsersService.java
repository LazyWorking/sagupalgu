package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportedUsersService {
    private final ReportedUsersRepository reportedUsersRepository;
    private final UserRepository userRepository;
    //유저 별로 grouping 된 신고 목록을 조회한다.
    public List<ReportedUserDTO> getReportedUsers() {
        return reportedUsersRepository.groupByUser();
    }

    //해당 유저에 대한 상세 신고 항목을 보여준다.
    public List<ReportedUsers> getTargetUser(Long userId) {
        User user = userRepository.findById(userId).get();
        return reportedUsersRepository.findByTargetUser(user);
    }

}
