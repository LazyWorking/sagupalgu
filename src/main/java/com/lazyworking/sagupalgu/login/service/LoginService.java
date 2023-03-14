package com.lazyworking.sagupalgu.login.service;

import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원 등록 처리
    @Transactional
    public Long signIn(SignInForm form){
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
}
