package com.lazyworking.sagupalgu.resources.service;

import com.lazyworking.sagupalgu.resources.domain.ResourceRole;
import com.lazyworking.sagupalgu.resources.repository.ResourceRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceRoleService {
    LinkedHashMap<String, List<String>> resourceRoleMappings = new LinkedHashMap<>();
    private final ResourceRoleRepository resourceRoleRepository;

    public List<ResourceRole> findAllResourceRoles() {
        return resourceRoleRepository.findAll();
    }

    public ResourceRole findRoleUserById(Long id) {
        return resourceRoleRepository.findById(id).orElseThrow(()-> new NoSuchElementException());
    }
    @Transactional
    public void createRoleUser(ResourceRole resourceRole) {
        resourceRoleRepository.save(resourceRole);
    }
    @Transactional
    public void deleteRoleUser(ResourceRole resourceRole) {
        resourceRoleRepository.delete(resourceRole);
    }


}
