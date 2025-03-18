package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.UpdatePasswordRequest;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.security.JWTUtil;  // لازم نعمل import للـJWTUtil
import com.example.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;  // الجديد - باش نخدمو بيه
    @Autowired
    private EmailService emailService;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByMail(request.getMail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getMotPasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        // ✅ Générer les tokens
        String accessToken = jwtUtil.generateToken(utilisateur.getMail(), "ROLE_" + utilisateur.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(utilisateur.getMail());

        // ✅ Stocker le refreshToken en base de données
        utilisateur.setRefreshToken(refreshToken);
        utilisateurRepository.save(utilisateur);

        System.out.println("Refresh Token en Base : " + utilisateur.getRefreshToken());

        // ✅ Retourner toutes les informations nécessaires, y compris l'email
        return ResponseEntity.ok(new LoginResponse(
                utilisateur.getIdU(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getMail(),  // ✅ On s'assure que l'email est bien renvoyé
                utilisateur.getRole(),
                accessToken,
                refreshToken
        ));
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
            emailService.envoyerEmailChangementMotDePasse(utilisateur, nouveauMotDePasse);
        } catch (Exception e) {
            return ResponseEntity.ok("✅ Mot de passe mis à jour avec succès ! (Erreur d'envoi de l'email)");
        }

        return ResponseEntity.ok("✅ Mot de passe mis à jour avec succès ! Un email de confirmation a été envoyé.");
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Refresh Token manquant !");
        }

        String mail = jwtUtil.extractMail(refreshToken);
        Utilisateur utilisateur = utilisateurRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // ✅ التحقق من `refreshToken` في `database`
        if (!refreshToken.equals(utilisateur.getRefreshToken())) {
            throw new RuntimeException("❌ Refresh Token invalide !");
        }

        // ✅ تأكد أن `refreshToken` لم تنتهي صلاحيته
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("❌ Refresh Token expiré !");
        }

        String newAccessToken = jwtUtil.generateToken(mail, utilisateur.getRole());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
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
