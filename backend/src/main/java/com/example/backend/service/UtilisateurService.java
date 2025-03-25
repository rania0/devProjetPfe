package com.example.backend.service;

import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;  // هذا جاي من SecurityConfig

    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByCin(utilisateur.getCin())) {
            throw new RuntimeException("Un utilisateur avec ce CIN existe déjà !");
        }

        // 1 - نجنري mot de passe عادي
        String rawPassword = generateRandomPassword();

        // 2 - نشفرو
        String hashedPassword = passwordEncoder.encode(rawPassword);

        // 3 - نخزنو المشفر في الـDB
        utilisateur.setMotPasse(hashedPassword);

        // 4 - ننظف الحقول حسب الـrole
        nettoyerChampsSelonRole(utilisateur);

        // 5 - نسجلو
        Utilisateur savedUser = utilisateurRepository.save(utilisateur);

        // 6 - نبعت الإيميل بالـmot de passe العادي (موش المشفر)
        try {
            emailService.envoyerEmailCreationUtilisateur(savedUser, rawPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'email");
        }

        return savedUser;
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 8);  // 8 caractères
    }

    private void nettoyerChampsSelonRole(Utilisateur user) {
        switch (user.getRole()) {
            case "fournisseur":
                user.setNumSecuritySocial(null);
                user.setNumConduit(null);
                user.setPointVente(null);
                if (user.getDateExpirationContract() != null && user.getDateExpirationContract().before(new Date())) {
                    user.setContractActif(false);
                } else {
                    user.setContractActif(true);
                }
                break;
            case "centre_regional":

                user.setNumSecuritySocial(null);
                user.setNumConduit(null);
                user.setPointVente(null);
                user.setNom(null);
                user.setPrenom(null);
                user.setDateEmbauche(null);
                user.setCin(null);
                user.setType(null);

                break;


            case "magasinier":
            case "admin":
                user.setNomSociete(null);
                user.setNumIdentificationEntreprise(null);
                user.setContractActif(null);
                user.setDateSignatureContract(null);
                user.setDateExpirationContract(null);
                user.setNumConduit(null);
                user.setPointVente(null);
                user.setCodePostal(null);
                user.setType(null);
                break;

            case "responsable_point_vente":
                user.setNomSociete(null);
                user.setNumIdentificationEntreprise(null);
                user.setContractActif(null);
                user.setDateSignatureContract(null);
                user.setDateExpirationContract(null);
                user.setNumConduit(null);
                user.setType(null);
                break;

            case "livreur":
                user.setNomSociete(null);
                user.setNumIdentificationEntreprise(null);
                user.setContractActif(null);
                user.setDateSignatureContract(null);
                user.setDateExpirationContract(null);
                user.setNumSecuritySocial(null);
                user.setPointVente(null);
                user.setCodePostal(null);
                break;
        }
    }
    public void supprimerUtilisateur(Long idU) {
        Utilisateur utilisateur = utilisateurRepository.findById(idU)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + idU));

        // حذف المستخدم
        utilisateurRepository.delete(utilisateur);

        // إرسال الإيميل يعلمو
        try {
            emailService.envoyerEmailSuppressionUtilisateur(utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'email de suppression");
        }
    }

    public void ajouterAdminParDefaut() {
        if (!utilisateurRepository.existsByMail("raniabenchaaben508@gmail.com")) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Super");
            admin.setPrenom("Admin");
            admin.setMail("raniabenchaaben508@gmail.com");
            admin.setMotPasse(passwordEncoder.encode("admin123"));
            admin.setRole("admin");

            utilisateurRepository.save(admin);
            System.out.println("✔️ Admin par défaut ajouté avec succès !");
        } else {
            System.out.println("⚠️ Admin par défaut existe déjà.");
        }
    }
    public List<Utilisateur> getAllUsersExcludingAdmin() {
        return utilisateurRepository.findAllNonAdminUsers();
    }

}
