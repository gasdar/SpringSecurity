package com.spring.security.app.persistence.entities;

import com.spring.security.app.persistence.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", unique=true, nullable = false, updatable = false)
    @Enumerated(value=EnumType.STRING)
    private RoleEnum roleEnum;

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="roles_permissions",
            joinColumns=@JoinColumn(name="role_id"),
            inverseJoinColumns=@JoinColumn(name="permission_id"),
            uniqueConstraints=@UniqueConstraint(columnNames={"role_id", "permission_id"})
    )
    private Set<PermissionEntity> permissions = new HashSet<>();

}
