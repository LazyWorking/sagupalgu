package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.ReportedUserDTO;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.user.repository.BlockedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Block;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockedUsersService {
    private final BlockedUsersRepository blockedUsersRepository;

    //차단 된 유저 목록을 불러온다.
    public List<BlockedUsers> getBlockedUsers() {
        return blockedUsersRepository.findAll();
    }

    //유저를 차단한다 --> 차단 목록에 유저를 추가한다.
    public BlockedUsers blockUser(BlockedUsers users) {
        BlockedUsers blockedUser = blockedUsersRepository.save(users);
        return blockedUser;
    }

    //차단된 유저를 해제한다.
    public void freeUser(long blockedUserId) {
        blockedUsersRepository.deleteById(blockedUserId);
    }

}
