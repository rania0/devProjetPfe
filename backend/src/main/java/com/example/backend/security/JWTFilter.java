//package com.example.backend.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class JWTFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTUtil jwtUtil;
//    private static final List<String> WHITELIST = List.of(
//            "/api/contract/sign",
//            "/api/contract/generate",
//            "/api/contract/uploadSignature",
//            "/api/responsable/confirm",
//            "/api/point_vente/all"
//    );
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return WHITELIST.stream().anyMatch(uri -> request.getRequestURI().startsWith(uri));
//    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String email = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7);
//            try {
//                email = jwtUtil.extractMail(token);
//            } catch (Exception e) {
//                System.out.println("Invalid Token: " + e.getMessage());
//            }
//        }
//
//        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.isTokenValid(token)) {
//                String role = jwtUtil.extractRole(token);
//                JWTAuthenticationToken authToken = new JWTAuthenticationToken(email, role);
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
package com.example.backend.security;

import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.repository.UtilisateurRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ResponsablePointDeVenteRepository responsablePointDeVenteRepository;

    private static final List<String> WHITELIST = List.of(
            "/api/contract/sign",
            "/api/contract/generate",
            "/api/contract/uploadSignature",
            "/api/responsable/confirm",
            "/api/point_vente/all",
            "/api/auth/login",
            "/api/auth/refresh"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return WHITELIST.stream().anyMatch(uri -> request.getRequestURI().startsWith(uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractMail(token);
            } catch (Exception e) {
                System.out.println("❌ Token invalide : " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(token)) {

                // 🔍 1. Cherche dans Utilisateur
                Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByMail(email);
                if (utilisateurOpt.isPresent()) {
                    String role = utilisateurOpt.get().getRole();
                    role = role.replaceFirst("^ROLE_", ""); // sécurité
                    JWTAuthenticationToken authToken = new JWTAuthenticationToken(email, role);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // 🔍 2. Sinon, cherche dans ResponsablePointDeVente
                    Optional<ResponsablePointDeVente> respOpt = responsablePointDeVenteRepository.findByEmail(email);
                    if (respOpt.isPresent()) {
                        String role = "responsable_point_vente";
                        JWTAuthenticationToken authToken = new JWTAuthenticationToken(email, role);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

