package com.spring.security.app.persistence.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spring.security.app.persistence.entities.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    List<RoleEntity> findByRoleEnumIn(Set<String> roles);

}
