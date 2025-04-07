package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.UpdatePasswordRequest;
import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.StatutResponsable;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.security.JWTUtil;  // لازم نعمل import للـJWTUtil
import com.example.backend.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ResponsablePointDeVenteRepository responsablePointDeVenteRepository;

    //@PostMapping("/login")
    //public ResponseEntity<?> login(@RequestBody LoginRequest request) {
       // Utilisateur utilisateur = utilisateurRepository.findByMail(request.getMail())
         //       .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

      //  if (!passwordEncoder.matches(request.getPassword(), utilisateur.getMotPasse())) {
       //     throw new RuntimeException("Mot de passe incorrect");
      //  }

       // String accessToken = jwtUtil.generateToken(utilisateur.getMail(), "ROLE_" + utilisateur.getRole());




       // String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getMail());

        // 🔥 نحتفظ بـ `refreshToken` في `database`
       // utilisateur.setRefreshToken(refreshToken);
       // utilisateurRepository.save(utilisateur);
        //System.out.println("Refresh Token en Base : " + utilisateur.getRefreshToken());
       // return ResponseEntity.ok(new LoginResponse(
        //        utilisateur.getIdU(),
        //        utilisateur.getNom(),
         //       utilisateur.getPrenom(),
         //       utilisateur.getMail(),
          //      utilisateur.getRole(),
          //      accessToken,
          //      refreshToken  // ✅ رجعنا `refreshToken` مع `response`
       // ));
  //  }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        Optional<Utilisateur> optionalUser = utilisateurRepository.findByMail(request.getMail());
//
//        if (optionalUser.isPresent()) {
//            Utilisateur utilisateur = optionalUser.get();
//
//            if (!passwordEncoder.matches(request.getPassword(), utilisateur.getMotPasse())) {
//                throw new RuntimeException("Mot de passe incorrect");
//            }
//
//            String accessToken = jwtUtil.generateToken(utilisateur.getMail(), "ROLE_" + utilisateur.getRole());
//            String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getMail());
//
//            utilisateur.setRefreshToken(refreshToken);
//            utilisateurRepository.save(utilisateur);
//
//            return ResponseEntity.ok(new LoginResponse(
//                    utilisateur.getIdU(),
//                    utilisateur.getNom(),
//                    utilisateur.getPrenom(),
//                    utilisateur.getMail(),
//                    utilisateur.getRole(),
//                    accessToken,
//                    refreshToken
//            ));
//        }
//
//        Optional<ResponsablePointDeVente> optionalResponsable = responsablePointDeVenteRepository.findByEmail(request.getMail());
//
//        if (optionalResponsable.isPresent()) {
//            ResponsablePointDeVente responsable = optionalResponsable.get();
//
//            if (!passwordEncoder.matches(request.getPassword(), responsable.getMotDePasse())) {
//                throw new RuntimeException("Mot de passe incorrect");
//            }
//
//            if (responsable.getStatus() != StatutResponsable.ACCEPTE) {
//                throw new RuntimeException("Votre demande n’a pas encore été acceptée.");
//            }
//
//            String role = "responsable_point_vente";
//            String accessToken = jwtUtil.generateToken(responsable.getEmail(), "ROLE_" + role);
//            String refreshToken = jwtUtil.generateRefreshToken(responsable.getEmail());
//
//            return ResponseEntity.ok(new LoginResponse(
//                    responsable.getId(),
//                    responsable.getNom(),
//                    responsable.getPrenom(),
//                    responsable.getEmail(),
//                    role,
//                    accessToken,
//                    refreshToken
//            ));
//
//        }
//
//        throw new RuntimeException("Utilisateur non trouvé");
//    }
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // 🔍 1. Essayer de trouver dans la table Utilisateur
    Optional<Utilisateur> optionalUser = utilisateurRepository.findByMail(request.getMail());

    if (optionalUser.isPresent()) {
        Utilisateur utilisateur = optionalUser.get();

        // 🔒 Vérification mot de passe
        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getMotPasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        // ✅ Générer les tokens
        String accessToken = jwtUtil.generateToken(utilisateur.getMail(), "ROLE_" + utilisateur.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getMail());

        utilisateur.setRefreshToken(refreshToken);
        utilisateurRepository.save(utilisateur);

        return ResponseEntity.ok(new LoginResponse(
                utilisateur.getIdU(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getMail(),
                utilisateur.getRole(),
                accessToken,
                refreshToken
        ));
    }

    // ❌ 2. Si non trouvé → chercher dans ResponsablePointDeVente
    Optional<ResponsablePointDeVente> optionalResponsable = responsablePointDeVenteRepository.findByEmail(request.getMail());

    if (optionalResponsable.isPresent()) {
        ResponsablePointDeVente responsable = optionalResponsable.get();

        if (!passwordEncoder.matches(request.getPassword(), responsable.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (responsable.getStatus() != StatutResponsable.ACCEPTE) {
            throw new RuntimeException("Votre demande n’a pas encore été acceptée.");
        }

        // ✅ Générer les tokens pour le responsable
        String accessToken = jwtUtil.generateToken(responsable.getEmail(), "ROLE_responsable_point_vente");
        String refreshToken = jwtUtil.generateRefreshToken(responsable.getEmail());

        return ResponseEntity.ok(new LoginResponse(
                responsable.getId(),
                responsable.getNom(),
                responsable.getPrenom(),
                responsable.getEmail(),
                "responsable_point_vente", // comme rôle
                accessToken,
                refreshToken
        ));
    }

    // ❌ Si non trouvé dans les 2 tables
    throw new RuntimeException("Utilisateur non trouvé");
}





    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        System.out.println("🔍 Requête reçue pour update password : " + request.getMail());
        Utilisateur utilisateur = utilisateurRepository.findByMail(request.getMail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 🔐 التحقق من صحة كلمة المرور القديمة
        if (!passwordEncoder.matches(request.getOldPassword(), utilisateur.getMotPasse())) {
            return ResponseEntity.badRequest().body("❌ Mot de passe actuel incorrect !");
        }

        // 🔑 تشفير كلمة المرور الجديدة وتحديثها في قاعدة البيانات
        String nouveauMotDePasse = request.getNewPassword();
        utilisateur.setMotPasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(utilisateur);

        try {
            // 📧 إرسال البريد الإلكتروني بعد التحديث
            emailService.envoyerEmailChangementMailSansMotDePasse(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.ok("✅ Mot de passe mis à jour avec succès ! (Erreur d'envoi de l'email)");
        }

        return ResponseEntity.ok("✅ Mot de passe mis à jour avec succès ! Un email de confirmation a été envoyé.");
    }
//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
//        String refreshToken = request.get("refreshToken");
//
//        if (refreshToken == null || refreshToken.isEmpty()) {
//            return ResponseEntity.badRequest().body("❌ Refresh Token manquant !");
//        }
//
//        String mail = jwtUtil.extractMail(refreshToken);
//        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
//                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
//
//        // ✅ التحقق من `refreshToken` في `database`
//        if (!refreshToken.equals(utilisateur.getRefreshToken())) {
//            throw new RuntimeException("❌ Refresh Token invalide !");
//        }
//
//        // ✅ تأكد أن `refreshToken` لم تنتهي صلاحيته
//        if (!jwtUtil.isTokenValid(refreshToken)) {
//            throw new RuntimeException("❌ Refresh Token expiré !");
//        }
//
//        String newAccessToken = jwtUtil.generateToken(mail, utilisateur.getRole());
//        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
//    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token manquant ou invalide");
        }

        String refreshToken = authHeader.substring(7);
        String mail = jwtUtil.extractUsername(refreshToken);  // ✅ On récupère le mail depuis le token

        // 🔍 1. Vérifie si l’utilisateur est dans la table `Utilisateur`
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByMail(mail);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();

            if (!refreshToken.equals(utilisateur.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token de rafraîchissement invalide");
            }

            // ✅ Corriger le double ROLE_
            String role = utilisateur.getRole(); // Ex: "ROLE_admin" ou "admin"
            role = role.replaceFirst("^ROLE_", ""); // Supprime "ROLE_" s’il est là
            String fullRole = "ROLE_" + role;

            String newAccessToken = jwtUtil.generateToken(mail, fullRole);
            return ResponseEntity.ok(Collections.singletonMap("accessToken", newAccessToken));
        }

        // 🔍 2. Sinon, cherche dans la table `ResponsablePointDeVente`
        Optional<ResponsablePointDeVente> respOpt = responsablePointDeVenteRepository.findByEmail(mail);
        if (respOpt.isPresent()) {
            ResponsablePointDeVente responsable = respOpt.get();

            // Pareil ici
            String role = "responsable_point_vente";
            role = role.replaceFirst("^ROLE_", ""); // sécurité
            String fullRole = "ROLE_" + role;

            String newAccessToken = jwtUtil.generateToken(mail, fullRole);
            return ResponseEntity.ok(Collections.singletonMap("accessToken", newAccessToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé");
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        String extractedToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractMail(extractedToken);

        Utilisateur utilisateur = utilisateurRepository.findByMail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // ✅ Supprimer le refreshToken en base
        utilisateur.setRefreshToken(null);
        utilisateurRepository.save(utilisateur);

        return ResponseEntity.ok("Déconnexion réussie !");
    }



}
