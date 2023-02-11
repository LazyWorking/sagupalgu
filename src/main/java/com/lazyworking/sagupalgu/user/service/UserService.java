package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.item.domain.UsedItem;
import com.lazyworking.sagupalgu.user.domain.Gender;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.form.UserEditForm;
import com.lazyworking.sagupalgu.user.form.UserSaveForm;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Long save(UserSaveForm form){
        User newUser = User.createUser(form.getName(),form.getEmail(),form.getPassword(),LocalDateTime.now(),form.getGender());
        newUser = userRepository.save(newUser);

        return newUser.getId();
    }

    public Long editUserInfo(UserEditForm form){
        User user = userRepository.findById(form.getId()).orElseThrow(()-> new NoSuchElementException());
        user.change(form.getName());
        return user.getId();
    }

    public Long changePassword(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
        user.setPassword(password);
        return user.getId();
    }

    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
    }
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
