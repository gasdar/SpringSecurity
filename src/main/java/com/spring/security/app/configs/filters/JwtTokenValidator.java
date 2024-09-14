package com.spring.security.app.configs.filters;

import java.io.IOException;
import java.util.Collection;

import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.spring.security.app.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

// El filtro no puede ser un componente, por ello que inyectamos el jwtUtils por constructor
public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtil jwtUtils;

    public JwtTokenValidator(JwtUtil jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if(jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = jwtToken.substring(7);
        DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

        if(decodedJWT != null) {
            String username = jwtUtils.extractUsername(decodedJWT);
            String authoritiesStr = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
    
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesStr);
    
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
    
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }
        
        filterChain.doFilter(request, response);
    }

}
