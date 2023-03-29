package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.user.domain.BlockedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.BlockedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlockedUsersService {
    private final BlockedUsersRepository blockedUsersRepository;
    private final UserRepository userRepository;

    //차단 된 유저 목록을 불러온다.
    public List<BlockedUsers> getBlockedUsers() {
        return blockedUsersRepository.findAll();
    }
    //유저를 차단한다 --> 차단 목록에 유저를 추가한다.
    @Transactional
    public Long blockUser(Long userId) {
        User user = userRepository.findById(userId).get();
        user.blockUser();
        BlockedUsers blockedUser = new BlockedUsers(user);
        blockedUser = blockedUsersRepository.save(blockedUser);

        return blockedUser.getId();
    }


    //차단된 유저를 해제한다.
    @Transactional
    public void freeUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException());
        BlockedUsers blockedUsers = blockedUsersRepository.findByUser(user);

        user.freeUser();
        blockedUsersRepository.delete(blockedUsers);
    }


    //차단 유저를 검색한다.
    public boolean findBlockedUserByUser(User user){
        //차단 되었으면 true를 반환한다.
        if(blockedUsersRepository.findByUser(user) != null)
            return true;
        return false;
    }

}
