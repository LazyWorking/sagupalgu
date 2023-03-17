package com.lazyworking.sagupalgu.resources.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ResourceRole {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public void setResourceRole(Resource resource, Role role) {
        this.resource = resource;
        this.role = role;
        if(!resource.getResourceRoles().contains(this))
            resource.getResourceRoles().add(this);
    }
}
