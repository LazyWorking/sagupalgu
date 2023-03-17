package com.lazyworking.sagupalgu.resources.service;

import com.lazyworking.sagupalgu.resources.domain.Role;
import com.lazyworking.sagupalgu.resources.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
    }

    public Role findRoleByName(String name) {
        return roleRepository.findByRoleName(name);
    }

    @Transactional
    public void createRole(Role role) {
        roleRepository.save(role);
    }
    @Transactional
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }
}
