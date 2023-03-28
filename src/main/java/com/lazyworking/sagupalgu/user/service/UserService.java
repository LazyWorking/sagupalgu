package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.admin.form.UserAllDataForm;
import com.lazyworking.sagupalgu.admin.form.UserManageForm;
import com.lazyworking.sagupalgu.global.security.service.AccountContext;
import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.domain.RoleUser;
import com.lazyworking.sagupalgu.admin.repository.RoleRepository;
import com.lazyworking.sagupalgu.admin.repository.RoleUserRepository;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.*;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ReportedUsersRepository reportedUsersRepository;

    private final RoleRepository roleRepository;

    private final RoleUserRepository roleUserRepository;

    @Transactional
    public Long addUser(SignInForm form){
        User newUser = form.toEntity();
        checkDuplicateEmail(form.getEmail());
        newUser = userRepository.save(newUser);

        Role role = roleRepository.findByRoleName("ROLE_USER");
        RoleUser roleUser = new RoleUser();
        roleUser.setRoleUser(newUser, role);
        roleUserRepository.save(roleUser);

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
    //회원 정보 변경 --> 관리자 전용
    @Transactional
    public Long editUserInfo_manage(UserManageForm userManageForm) {
        User user = userRepository.findById(userManageForm.getId()).orElseThrow(()-> new NoSuchElementException());
        List<Role> currentRoles = user.getRoleUsers().stream().map((roleUser)-> roleUser.getRole()).collect(Collectors.toList());
        if(!userManageForm.getRoles().equals(currentRoles)){
            //기존 권한 삭제
            for (RoleUser roleUser : user.getRoleUsers()) {
                roleUserRepository.delete(roleUser);
            }
            user.getRoleUsers().clear();
            //새로운 권한 부여
            userManageForm.getRoles().forEach(
                    (role) -> {
                        RoleUser roleUser = new RoleUser();
                        roleUser.setRoleUser(user, role);
                        roleUserRepository.save(roleUser);
                    }
            );
        }
        //회원 정보 변경
        user.changeEverything(userManageForm.getName(), userManageForm.getEmail(), user.getPassword(), userManageForm.getGender());
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
    //관리자 전용 회원 조회
    public UserManageForm findUserManageForm(Long id){
        User user = findUser(id);
        List<Role> roles = user.getRoleUsers().stream().map((roleUser) -> roleUser.getRole()).collect(Collectors.toList());
        UserManageForm userManageForm = new UserManageForm(user.getId(),user.getName(),user.getEmail(),user.getPassword(),user.getGender(),roles);
        return userManageForm;
    }
    //모든 정보가 포함된 유저의 상세 정보
    public UserAllDataForm findUserAllDataForm(Long id) {
        User user = findUser(id);
        List<Role> roles = user.getRoleUsers().stream().map((roleUser) -> roleUser.getRole()).collect(Collectors.toList());
        UserAllDataForm userAllDataForm = new UserAllDataForm(user.getId(),user.getJoinDate(),user.getName(),user.getEmail(),user.getPassword(),user.getGender(),roles);
        return userAllDataForm;
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
