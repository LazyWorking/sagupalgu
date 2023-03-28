package com.lazyworking.sagupalgu.admin.repository;

import com.lazyworking.sagupalgu.admin.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource,Long> {

    @Query("select r from Resource r join fetch r.resourceRoles rr join fetch rr.role where r.resourceType= 'url' order by r.orderNum desc")
    List<Resource> findAllUrlResources();
}
