package com.lazyworking.sagupalgu.resources.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"parentName", "roleHierarchy"})
public class RoleHierarchy {

    @Id
    @GeneratedValue
    private Long id;
    //자식 권한
    @Column(name = "child_role")
    private String childRole;
    //부모 권한
    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_role", referencedColumnName = "child_role")
    private RoleHierarchy parentRole;

    @OneToMany(mappedBy = "parentRole", cascade={CascadeType.ALL})
    private Set<RoleHierarchy> roleHierarchy = new HashSet<RoleHierarchy>();
}