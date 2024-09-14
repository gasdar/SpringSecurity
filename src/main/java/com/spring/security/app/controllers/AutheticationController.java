package com.spring.security.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.app.dtos.UserRequestCreateDto;
import com.spring.security.app.dtos.UserResponseAuthDto;
import com.spring.security.app.dtos.UserRequestLoginDto;
import com.spring.security.app.services.UserDetailsServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value="/auth")
public class AutheticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping(value="/sign-up")
    public ResponseEntity<UserResponseAuthDto> register(@RequestBody @Valid UserRequestCreateDto userRequest) {
        return new ResponseEntity<>(this.userDetailsService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping(value="/log-in")
    public ResponseEntity<UserResponseAuthDto> login(@RequestBody @Valid UserRequestLoginDto userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

}
