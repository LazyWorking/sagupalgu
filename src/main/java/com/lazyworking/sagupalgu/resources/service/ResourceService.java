package com.lazyworking.sagupalgu.resources.service;

import com.lazyworking.sagupalgu.resources.domain.Resource;
import com.lazyworking.sagupalgu.resources.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public List<Resource> findAllResource(){
        return resourceRepository.findAll();
    }

    public Resource findResource(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }
    @Transactional
    public void createResource(Resource resource) {
        resourceRepository.save(resource);
    }
    @Transactional
    public void deleteResource(Resource resource) {
        resourceRepository.delete(resource);
    }
}
