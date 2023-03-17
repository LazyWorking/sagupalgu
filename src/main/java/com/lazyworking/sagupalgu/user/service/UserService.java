package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.resources.domain.RoleUser;
import com.lazyworking.sagupalgu.resources.repository.RoleRepository;
import com.lazyworking.sagupalgu.resources.repository.RoleUserRepository;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.ReportUserForm;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserPasswordForm;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final ReportedUsersRepository reportedUsersRepository;

    private final RoleRepository roleRepository;

    private final RoleUserRepository roleUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long addUser(SignInForm form){
        User newUser = form.toEntity();
        newUser.setPassword(passwordEncoder.encode(form.getPassword()));
        checkDuplicateEmail(form.getEmail());
        newUser = userRepository.save(newUser);
        return newUser.getId();
    }
    //중복 이메일 확인
    private void checkDuplicateEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null)
            throw new IllegalStateException("이미 등록된 회원입니다");
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
        User targetUser = userRepository.findByEmail(reportUserForm.getTargetUserEmail());
        if (targetUser == null) {
            throw new IllegalStateException("신고 대상자 email이 잘못되었습니다.");
        }

        ReportedUsers reportedUsers = new ReportedUsers(reporter, targetUser, reportUserForm.getReportContext());
        reportedUsersRepository.save(reportedUsers);

        return reportedUsers.getId();
    }
}
