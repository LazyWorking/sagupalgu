package com.lazyworking.sagupalgu.resources.service;


import com.lazyworking.sagupalgu.resources.domain.RoleUser;
import com.lazyworking.sagupalgu.resources.repository.RoleUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleUserService {
    private final RoleUserRepository roleUserRepository;

    public List<RoleUser> findAllRoleUsers() {
        return roleUserRepository.findAll();
    }

    public RoleUser findRoleUserById(Long id) {
        return roleUserRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
    }

    public void createRoleUser(RoleUser roleUser) {
        roleUserRepository.save(roleUser);
    }

    public void deleteRoleUser(RoleUser roleUser) {
        roleUserRepository.delete(roleUser);
    }

}
