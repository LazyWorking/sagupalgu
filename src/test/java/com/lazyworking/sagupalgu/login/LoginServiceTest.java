package com.lazyworking.sagupalgu.login;

import com.lazyworking.sagupalgu.login.form.LoginForm;
import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.login.service.LoginService;
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
import lombok.RequiredArgsConstructor;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class LoginServiceTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("회원 등록")
    void save() {
        //given
        User user= new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        SignInForm form = new SignInForm(user.getName(),user.getEmail(),user.getPassword(),user.getGender());

        //when
        Long savedUserId = loginService.signIn(form);
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
                () -> loginService.signIn(form)
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원 로그인")
    void login() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));

        //when
        LoginForm form = new LoginForm(user.getEmail(), user.getPassword());
        Boolean loginStatus = loginService.login(form);

        //then
        assertThat(loginStatus).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 로그인 실패")
    void loginFail() {
        //given
        User user = userRepository.save(new User("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M));

        //when
        LoginForm form = new LoginForm(user.getEmail(), "12345");
        Boolean loginStatus = loginService.login(form);

        //then
        assertThat(loginStatus).isEqualTo(false);
    }

}
