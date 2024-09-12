package com.spring.security.app.persistence.repositories;

import com.spring.security.app.persistence.entities.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity AS u WHERE u.username=?1")
    Optional<UserEntity> findUser(String username);

}
