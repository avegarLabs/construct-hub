package com.avegarlabs.construct_hub.infrastructure.utils;

import com.avegarlabs.construct_hub.infrastructure.config.JwtProperties;
import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

   // String key = "9A3sH7dL8X@z!4vT2pQr1Uc#9kYfMmEwG0bCxZ6JnLrVoPsQ";
    //int time = 86400000;
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(userPrincipal.getUsername()) // Sin "set"
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()  // Obtiene el cuerpo (claims)
                .getSubject(); // Extrae el username
    }

    public boolean validateToken(String token) {
        try {
           SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

            // Usa el parser moderno
            Jwts.parser()
                    .verifyWith(secretKey)  // <-- ¡Nueva sintaxis!
                    .build()
                    .parseSignedClaims(token); // <-- Cambió el nombre del método

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
