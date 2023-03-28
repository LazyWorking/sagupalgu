package com.lazyworking.sagupalgu.admin.service;

import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.form.RoleAddForm;
import com.lazyworking.sagupalgu.admin.form.RoleForm;
import com.lazyworking.sagupalgu.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public Long createRole(RoleAddForm roleAddForm) {
        Role role = new Role(roleAddForm.getRoleName(), roleAddForm.getRoleDesc());
        roleRepository.save(role);
        return role.getId();
    }
    @Transactional
    public Long change(RoleForm roleForm){
        Role role = roleRepository.findById(roleForm.getId()).orElseThrow(NoSuchElementException::new);
        role.change(roleForm);
        return role.getId();
    }
    @Transactional
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    @Transactional
    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }
}
