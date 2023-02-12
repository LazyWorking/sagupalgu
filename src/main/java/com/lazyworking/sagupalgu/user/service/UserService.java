package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserLoginForm;
import com.lazyworking.sagupalgu.user.form.UserPasswordForm;
import com.lazyworking.sagupalgu.user.form.UserSaveForm;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    //회원 등록 처리
    @Transactional
    public Long save(UserSaveForm form){
        User newUser = form.toEntity();
        checkDuplicateEmail(form.getEmail());
        newUser = userRepository.save(newUser);
        return newUser.getId();
    }
    //중복 이메일 확인
    private void checkDuplicateEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        if(!users.isEmpty())
            throw new IllegalStateException("이미 등록된 회원입니다");
    }
    //회원 정보 수정
    @Transactional
    public Long editUserInfo(UserEditForm form){
        User user = userRepository.findById(form.getId()).orElseThrow(()-> new NoSuchElementException());
        user.change(form.getName());
        return user.getId();
    }
    //회원 비밀번호 변경
    @Transactional
    public Long changePassword(UserPasswordForm form) {
        User user = userRepository.findById(form.getId()).orElseThrow(()-> new NoSuchElementException());
        user.changePassword(form.getPassword());
        return user.getId();
    }
    //회원 목록 조회
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
    //회원 조회
    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
    }
    //회원 삭제
    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
    //회원 로그인 처리
    public Boolean login(UserLoginForm form) {
        List<User> users = userRepository.findByEmail(form.getEmail());
        //이메일에 대응되는 회원이 없는 경우
        if(users.isEmpty())
            return false;
        User user = users.get(0);
        //비밀번호가 틀린 경우
        if(user.getPassword() != form.getPassword())
            return false;

        return true;
    }

}
