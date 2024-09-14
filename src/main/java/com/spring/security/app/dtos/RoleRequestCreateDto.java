package com.spring.security.app.dtos;

import java.util.Set;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Size;

@Validated
public record RoleRequestCreateDto(@Size(max=3, message="The user cannot have more than 3 roles") Set<String> roleNames) {

}
