package com.spring.security.app.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value={"username", "message", "jwt", "status"})
public record UserResponseAuthDto(
    String username,
    String message,
    String jwt,
    boolean status
) {

}
