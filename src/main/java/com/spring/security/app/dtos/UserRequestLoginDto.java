package com.spring.security.app.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequestLoginDto(
    @NotBlank String username,
    @NotBlank String password
) {

}
