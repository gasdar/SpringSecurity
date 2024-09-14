package com.spring.security.app.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record UserRequestCreateDto(
    @NotBlank String username,
    @NotBlank String password,
    @Valid RoleRequestCreateDto rolesRequest
) {

}
