package com.example.backend.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String role;

    public JWTAuthenticationToken(String email, String role) {
        super(Collections.singletonList(new SimpleGrantedAuthority(
                (role != null && !role.isEmpty()) ?
                        (role.startsWith("ROLE_") ? role : "ROLE_" + role) : "ROLE_USER" // 🔥 Correction
        )));
        this.email = email;
        this.role = (role != null && !role.isEmpty()) ?
                (role.startsWith("ROLE_") ? role : "ROLE_" + role) : "ROLE_USER"; // 🔥 Correction
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
