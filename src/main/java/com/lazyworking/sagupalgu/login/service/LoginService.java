package com.lazyworking.sagupalgu.login.service;

import com.lazyworking.sagupalgu.login.form.LoginForm;
import com.lazyworking.sagupalgu.login.form.SignInForm;
import com.lazyworking.sagupalgu.user.domain.User;
import com.lazyworking.sagupalgu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final UserRepository userRepository;

    //회원 등록 처리
    @Transactional
    public Long signIn(SignInForm form){
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
    //회원 로그인 처리
    public Boolean login(LoginForm form) {
        List<User> users = userRepository.findByEmail(form.getEmail());
        //이메일에 대응되는 회원이 없는 경우
        if(users.isEmpty())
            return false;
        User user = users.get(0);

        //비밀번호가 틀린 경우
        if(!user.getPassword().equals(form.getPassword()))
            return false;

        return true;
    }
}
