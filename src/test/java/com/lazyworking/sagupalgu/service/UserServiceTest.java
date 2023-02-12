package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.item.service.UsedItemService;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserSaveForm;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import com.lazyworking.sagupalgu.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    @DisplayName("회원 등록")
    void save() {
        //given
        User user= User.createUser("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        UserSaveForm form = new UserSaveForm(user);

        //when
        Long savedUserId = userService.save(form);
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
    @DisplayName("회원 삭제")
    void deleteById() {
        //given
        User user= User.createUser("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        UserSaveForm form = new UserSaveForm(user);
        Long savedUserId = userService.save(form);

        //when
        userService.deleteUser(savedUserId);

        //then
        //이미 삭제한 아이템에 대한 조회를 수행한 경우 NoSuchElementException 에러가 발생하게 된다.
        assertThatThrownBy(() -> {
            User searchUser = userService.findUser(savedUserId);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 정보 수정")
    void editUser() {
        //given
        User user= User.createUser("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        UserSaveForm form = new UserSaveForm(user);
        Long savedUserId = userService.save(form);
        user = userService.findUser(savedUserId);

        //when
        user.setName("user2");
        UserEditForm editForm = new UserEditForm(user);
        Long editedUserId = userService.editUserInfo(editForm);
        User editedUser = userService.findUser(editedUserId);

        //then
        assertThat(editedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    void changePassword() {
        //given
        User user= User.createUser("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        UserSaveForm form = new UserSaveForm(user);
        Long savedUserId = userService.save(form);
        user = userService.findUser(savedUserId);

        //when
        user.setPassword("4321");
        UserEditForm editForm = new UserEditForm(user);
        Long editedUserId = userService.changePassword(user.getId(), user.getPassword());
        User editedUser = userService.findUser(editedUserId);

        //then
        assertThat(editedUser).isEqualTo(user);
    }




    @Test
    @DisplayName("회원 목록 조회")
    void findAllUsedItems() {
        //given
        User user1= User.createUser("user1", "user@email.com", "1234", LocalDateTime.now(), Gender.M);
        UserSaveForm form1 = new UserSaveForm(user1);

        User user2= User.createUser("user2", "user2@email.com", "12345", LocalDateTime.now(), Gender.W);
        UserSaveForm form2 = new UserSaveForm(user2);

        //when
        userService.save(form1);
        userService.save(form2);

        //then
        assertThat(userService.findUsers().size()).isEqualTo(2);
    }
}