package com.lazyworking.sagupalgu.admin.service;

import com.lazyworking.sagupalgu.admin.domain.RoleHierarchy;
import com.lazyworking.sagupalgu.admin.repository.RoleHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleHierarchyService{
    private final RoleHierarchyRepository roleHierarchyRepository;

    //spring에서 처리할 수 있는 형태로 계층 정보를 표현하는 작업을 수행한다. child_role < parent_role 과 같은 형식으로 만든다.
    public String findAllHierarchy() {
        List<RoleHierarchy> roleHierarchyList = roleHierarchyRepository.findAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (RoleHierarchy roleHierarchy : roleHierarchyList) {
            if(roleHierarchy.getParentRole() == null)
                continue;
            stringBuilder.append(roleHierarchy.getParentRole().getChildRole());
            stringBuilder.append(" > ");
            stringBuilder.append(roleHierarchy.getChildRole());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
