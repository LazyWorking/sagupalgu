package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.BlockedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockedUsersService {
    private final BlockedUsersRepository blockedUsersRepository;
    private final UserRepository userRepository;

    //차단 된 유저 목록을 불러온다.
    public List<BlockedUsers> getBlockedUsers() {
        return blockedUsersRepository.findAll();
    }

    //유저를 차단한다 --> 차단 목록에 유저를 추가한다.
    public Long blockUser(Long userId) {
        User user = userRepository.findById(userId).get();
        BlockedUsers blockedUser = new BlockedUsers(user);
        blockedUser = blockedUsersRepository.save(blockedUser);

        return blockedUser.getId();
    }

    //차단된 유저를 해제한다.
    public void freeUser(long blockedUserId) {
        blockedUsersRepository.deleteById(blockedUserId);
    }

}
