package com.spring.security.app.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;
    private String password;

    @Column(name="is_enabled")
    private boolean enabled;

    @Column(name="is_account_non_expired")
    private boolean accountNonExpired;

    @Column(name="is_account_non_looked")
    private boolean accountNonLooked;

    @Column(name="is_credentials_non_expired")
    private boolean credentialsNonExpired;

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"),
            uniqueConstraints=@UniqueConstraint(columnNames={"user_id", "role_id"})
    )
    private Set<RoleEntity> roles = new HashSet<>();

}
