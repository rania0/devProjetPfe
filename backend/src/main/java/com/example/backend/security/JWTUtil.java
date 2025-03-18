package com.example.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${app.jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);  // Vérifier qu'on ne fait pas un double encodage
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Générer un token d'accès avec "ROLE_"
    public String generateToken(String mail, String role) {
        System.out.println("ROLE ENCODED: " + role); // 🔥 Ajouté pour DEBUG
        return Jwts.builder()
                .setSubject(mail)
                .claim("role", "ROLE_" + role) // ✅ On ajoute toujours "ROLE_"
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }



    // ✅ Générer un refresh token
    public String generateRefreshToken(String mail) {
        return Jwts.builder()
                .setSubject(mail)

                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expiration 24 heures
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractMail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
