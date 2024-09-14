package com.spring.security.app.utils;

import static com.spring.security.app.configs.PropertyFileConfig.LOGGER;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {

    @Value(value="${spring.security.private-jwt-key}")
    private String privateKey;

    @Value(value="${spring.security.key-generated-by-user}")
    private String keyGeneratedByUser;

    public String createToken(Authentication authentication) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            String username = authentication.getPrincipal().toString();
            String authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));
    
            String jwtToken = JWT.create()
                .withIssuer(keyGeneratedByUser)
                .withSubject(username)
                .withClaim("authorities", authorities)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 30)))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);

            return jwtToken;
        } catch(JWTCreationException e) {
            throw new JWTVerificationException("Imposible generating token");
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(this.keyGeneratedByUser)
                .build();

            return verifier.verify(token);
        }
        catch (JWTVerificationException e){
            LOGGER.warning("Token invalid, not authorized");
            return null;
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

}
