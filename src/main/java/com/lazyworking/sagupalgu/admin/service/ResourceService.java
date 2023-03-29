package com.lazyworking.sagupalgu.admin.service;

import com.lazyworking.sagupalgu.admin.domain.Resource;
import com.lazyworking.sagupalgu.admin.domain.ResourceRole;
import com.lazyworking.sagupalgu.admin.domain.Role;
import com.lazyworking.sagupalgu.admin.form.ResourceAddForm;
import com.lazyworking.sagupalgu.admin.form.ResourceForm;
import com.lazyworking.sagupalgu.admin.repository.ResourceRepository;
import com.lazyworking.sagupalgu.admin.repository.ResourceRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceRoleRepository resourceRoleRepository;

    public List<Resource> findAllResource(){
        return resourceRepository.findAll();
    }

    public Resource findResource(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public ResourceForm getResourceForm(Long id) {
        Resource resource = findResource(id);
        List<Role> roles = resource.getResourceRoles().stream().map(resourceRole -> resourceRole.getRole()).collect(Collectors.toList());

        return new ResourceForm(resource.getId(), resource.getOrderNum(), resource.getResourceName(), resource.getHttpMethod(), resource.getResourceType(), roles);
    }

    @Transactional
    public Long change(ResourceForm resourceForm) {
        Resource resource = findResource(resourceForm.getId());
        List<Role> currentRoles = resource.getResourceRoles().stream().map(resourceRole -> resourceRole.getRole()).collect(Collectors.toList());
        log.info("currentRoles:{},changedRoles:{}", currentRoles, resourceForm.getRoles());
        if (!currentRoles.equals(resourceForm.getRoles())) {
            //기존에 설정된 권한 삭제
            for (ResourceRole resourceRole : resource.getResourceRoles()) {
                resourceRoleRepository.delete(resourceRole);
            }
            resource.getResourceRoles().clear();
            for (Role role : resourceForm.getRoles()) {
                ResourceRole resourceRole = new ResourceRole();
                resourceRole.setResourceRole(resource, role);
                resourceRoleRepository.save(resourceRole);
            }
        }
        resource.changeResourceInfo(resourceForm);
        return resourceForm.getId();
    }
    @Transactional
    public void createResource(Resource resource) {
        resourceRepository.save(resource);
    }

    @Transactional
    public Long createResource(ResourceAddForm resourceAddForm) {
        Resource resource=new Resource(resourceAddForm.getResourceName(), resourceAddForm.getHttpMethod(), resourceAddForm.getResourceType(), resourceAddForm.getOrderNum());
        resourceRepository.save(resource);
        for (Role role : resourceAddForm.getRoles()) {
            ResourceRole resourceRole = new ResourceRole();
            resourceRole.setResourceRole(resource, role);
            resourceRoleRepository.save(resourceRole);
        }
        return resource.getId();
    }
    @Transactional
    public void deleteResource(Resource resource) {
        resourceRepository.delete(resource);
    }

    @Transactional
    public void deleteResourceById(Long id) {
        resourceRepository.deleteById(id);
    }
}
