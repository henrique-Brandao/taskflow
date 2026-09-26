package com.henrique.taskflow.service;

import com.henrique.taskflow.dto.response.LoginResponse;
import com.henrique.taskflow.model.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

public class TokenService {

    private final Key key;

    public TokenService() {
        String secret = System.getenv("JWT_SECRET");
        if (secret == null || secret.isEmpty()) {
            // For production, this should be an environment variable. 
            // Using a fixed generated key for demo will invalidate tokens on cold starts!
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            this.key = Keys.hmacShaKeyFor(decodedKey);
        }
    }

    public LoginResponse generateToken(AppUser user) {
        var now = Instant.now();
        var expiresIn = 86400L;

        String jwtValue = Jwts.builder()
                .setIssuer("TaskFlow")
                .setSubject(user.getId())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expiresIn)))
                .signWith(key)
                .compact();

        return new LoginResponse(jwtValue, expiresIn);
    }

    public String validateTokenAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null; // Invalid token
        }
    }
}
