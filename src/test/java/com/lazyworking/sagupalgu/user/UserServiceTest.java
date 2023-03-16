package com.lazyworking.sagupalgu.user;

import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.ReportUserForm;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserPasswordForm;
import com.lazyworking.sagupalgu.user.repository.ReportedUsersRepository;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import com.lazyworking.sagupalgu.user.service.ReportedUsersService;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportedUsersService reportedUsersService;

    @Autowired
    private ReportedUsersRepository reportedUsersRepository;
    @Test
    @DisplayName("회원 등록")
    void save() {
        //given
        User user= new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        SignInForm form = new SignInForm(user.getName(),user.getEmail(),user.getPassword(),user.getGender());

        //when
        Long savedUserId = userService.addUser(form);
        User savedUser = userService.findUser(savedUserId);

        //then
        log.info("user: {}, saved user:{}", user.getJoinDate(), savedUser.getJoinDate());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(savedUser.getJoinDate()).isEqualTo(user.getJoinDate());
        assertThat(savedUser.getGender()).isEqualTo(user.getGender());

        log.info("savedItem: {}", user);
    }

    @Test
    @DisplayName("중복 이메일 회원 등록")
    void save_duplicate_email() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));
        User user2= new User("user2", "user@email.com", "1235", LocalDateTime.now(), Gender.F);
        SignInForm form = new SignInForm(user.getName(),user.getEmail(),user.getPassword(),user.getGender());

        //when
        //then
        assertThatThrownBy(
                () -> userService.addUser(form)
        ).isInstanceOf(IllegalStateException.class);
    }
    @Test
    @DisplayName("회원 삭제")
    void deleteById() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));

        //when
        userService.deleteUser(user.getId());

        //then
        //이미 삭제한 아이템에 대한 조회를 수행한 경우 NoSuchElementException 에러가 발생하게 된다.
        assertThatThrownBy(() -> {
            User searchUser = userService.findUser(user.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 정보 수정")
    void editUser() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));

        //when
        user.setName("user2");
        UserEditForm editForm = new UserEditForm(user.getId(),user.getName(),user.getEmail(),user.getPassword(),user.getGender());
        Long editedUserId = userService.editUserInfo(editForm);
        User editedUser = userService.findUser(editedUserId);

        //then
        assertThat(editedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    void changePassword() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));

        //when
        user.setPassword("4321");
        UserPasswordForm passwordForm = new UserPasswordForm(user.getId(),user.getPassword());
        Long editedUserId = userService.changePassword(passwordForm);
        User editedUser = userService.findUser(editedUserId);

        //then
        assertThat(editedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 목록 조회")
    void findAllUsedItems() {
        //given
        userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));
        userRepository.save(new User("user2", "user2@email.com", "12345", LocalDateTime.now(), Gender.F));

        //when
        List<User> users = userService.findUsers();

        //then
        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원 신고하기")
    void reportUser() {
        //given
        User user1 = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));
        User user2 = userRepository.save(new User("user2", "user2@email.com", "12345", LocalDateTime.now(), Gender.F));

        //when
        ReportUserForm reportUserForm = new ReportUserForm(user1.getId(),user2.getEmail(),"부적절한 언어 사용");
        Long reportedUsersId = userService.reportUser(reportUserForm);
        ReportedUsers reportedUser = reportedUsersRepository.findById(reportedUsersId).get();

        //then
        assertThat(reportedUser.getReporter()).isEqualTo(user1);
        assertThat(reportedUser.getTargetUser()).isEqualTo(user2);
        assertThat(reportedUser.getContext()).isEqualTo(reportUserForm.getReportContext());
    }

}