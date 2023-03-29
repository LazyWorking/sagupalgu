package com.lazyworking.sagupalgu.admin.domain;

import com.lazyworking.sagupalgu.admin.form.ResourceForm;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Resource {
    @Id
    @GeneratedValue
    private Long id;

    private String resourceName;

    private String httpMethod;

    private String resourceType;

    private int orderNum;

    //연관관계 메소드
    @OneToMany(mappedBy = "resource",fetch= FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ResourceRole> resourceRoles = new ArrayList<>();

    public Resource(String resourceName, String httpMethod, String resourceType, int orderNum) {
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        this.resourceType = resourceType;
        this.orderNum = orderNum;
    }
    //변경 메소드
    public void changeResourceInfo(ResourceForm resourceForm){
        this.resourceName = resourceForm.getResourceName();
        this.httpMethod = resourceForm.getHttpMethod();
        this.resourceType = resourceForm.getResourceType();
        this.orderNum = resourceForm.getOrderNum();
    }
}
