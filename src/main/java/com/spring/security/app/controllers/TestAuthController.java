package com.spring.security.app.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/auth")
@PreAuthorize(value="denyAll()")
public class TestAuthController {

    @GetMapping(value="/get")
    @PreAuthorize(value="hasAuthority('READ')")
    public String helloGet() {
        return "Hello World - GET";
    }

    @PostMapping(value="/post")
    @PreAuthorize(value="hasAuthority('UPDATE') or hasAuthority('CREATE')")
    public String helloPost() {
        return "Hello World - POST";
    }
    
    @PutMapping(value="/put")
    @PreAuthorize(value="hasAnyAuthority('UPDATE', 'DELETE')")
    public String helloPut() {
        return "Hello World - PUT";
    }

    @DeleteMapping(value="/delete")
    @PreAuthorize(value="hasAnyRole('ADMIN', 'DEVELOPER')")
    public String helloDelete() {
        return "Hello World - DELETE";
    }

    @PatchMapping(value="/patch")
    @PreAuthorize(value="hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "Hello World - PATCH";
    }

}
