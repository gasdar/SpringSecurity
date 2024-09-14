package com.spring.security.app.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.security.app.dtos.UserRequestCreateDto;
import com.spring.security.app.dtos.UserRequestLoginDto;
import com.spring.security.app.dtos.UserResponseAuthDto;
import com.spring.security.app.persistence.entities.RoleEntity;
import com.spring.security.app.persistence.entities.UserEntity;
import com.spring.security.app.persistence.repositories.RoleRepository;
import com.spring.security.app.persistence.repositories.UserRepository;
import com.spring.security.app.utils.JwtUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow( () -> 
            new UsernameNotFoundException("El usuario " + username + ", no existe."));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        userEntity.getRoles().forEach(role -> 
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.isEnabled(),
            userEntity.isAccountNonExpired(),
            userEntity.isCredentialsNonExpired(),
            userEntity.isAccountNonLooked(),
            authorities);
    }

    public UserResponseAuthDto createUser(UserRequestCreateDto userRequest) {
        String username = userRequest.username();
        String password = userRequest.password();
        Set<String> roles = userRequest.rolesRequest().roleNames();

        Set<RoleEntity> rolesDB = roleRepository.findByRoleEnumIn(roles).stream().collect(Collectors.toSet());

        if(rolesDB.isEmpty()) {
            throw new IllegalArgumentException("The specified roles don't exist");
        }

        UserEntity newUser = UserEntity.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .roles(rolesDB)
            .enabled(true)
            .accountNonExpired(true)
            .credentialsNonExpired(true)
            .accountNonLooked(true)
            .build();
        
        newUser = userRepository.save(newUser);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        newUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        newUser.getRoles()
            .stream()
            .flatMap(role -> role.getPermissions().stream())
            .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getUsername(), newUser.getPassword(), authorities);
        String token = jwtUtils.createToken(authentication);

        UserResponseAuthDto userResponse = new UserResponseAuthDto(username,
            "User created successfully",
            token,
            true);

        return userResponse;
    }

    public UserResponseAuthDto loginUser(UserRequestLoginDto loginUserDto) {
        String username = loginUserDto.username();
        String password = loginUserDto.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        UserResponseAuthDto authUserDto = new UserResponseAuthDto(
            username,
            "User logged successfully",
            accessToken,
            true);

        return authUserDto;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials (password)");
        }

        return new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(),
            userDetails.getPassword(),
            userDetails.getAuthorities());
    }

}
