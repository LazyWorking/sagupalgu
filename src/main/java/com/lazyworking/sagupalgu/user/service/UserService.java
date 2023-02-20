package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.login.form.LoginForm;
import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.*;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ReportedUsersRepository reportedUsersRepository;

    @PostConstruct
    void initData() {
        User user1= new User("user1","user@email.com","12345678910", LocalDateTime.now(),Gender.M);
        User user2= new User("user2","user2@email.com","12345678911", LocalDateTime.now(),Gender.F);

        userRepository.save(user1);
        userRepository.save(user2);
    }


    //회원 정보 수정
    @Transactional
    public Long editUserInfo(UserEditForm form){
        User user = userRepository.findById(form.getId()).orElseThrow(()-> new NoSuchElementException());
        user.change(form.getName(),form.getGender());
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

    @Transactional
    public Long reportUser(ReportUserForm reportUserForm) {
        User reporter = userRepository.findById(reportUserForm.getReporterUserId()).get();
        List<User> targetUsers = userRepository.findByEmail(reportUserForm.getTargetUserEmail());

        if (targetUsers.isEmpty()) {
            throw new IllegalStateException("신고 대상자 email이 잘못되었습니다.");
        }

        ReportedUsers reportedUsers = new ReportedUsers(reporter, targetUsers.get(0), reportUserForm.getReportContext());
        reportedUsersRepository.save(reportedUsers);

        return reportedUsers.getId();
    }
}
