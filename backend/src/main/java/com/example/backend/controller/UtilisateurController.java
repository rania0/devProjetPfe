package com.example.backend.controller;

import com.example.backend.dto.RefusDTO;
import com.example.backend.dto.ResponsablePointDeVenteDTO;
import com.example.backend.dto.UpdateUserRequest;
import com.example.backend.dto.UtilisateurSummaryDTO;
import com.example.backend.entity.PtVente;
import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.PtVenteRepository;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.repository.UtilisateurRepository;
import com.example.backend.security.JWTUtil;
import com.example.backend.service.EmailService;
import com.example.backend.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.example.backend.service.ResponsablePointDeVenteService;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/admin")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PtVenteRepository ptVenteRepository;
    @Autowired
    private ResponsablePointDeVenteService responsablePointVenteService;
    @Autowired
    private ResponsablePointDeVenteService responsableService;
    @Autowired
    private ResponsablePointDeVenteRepository responsableRepository;



    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    // Endpoint pour ajouter un utilisateur (POST)
    @PostMapping("/ajouter")
    public ResponseEntity<?> ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur savedUser = utilisateurService.ajouterUtilisateur(utilisateur);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/supprimer/{idU}")
    public ResponseEntity<String> supprimerUtilisateur(@PathVariable Long idU) {
        utilisateurService.supprimerUtilisateur(idU);
        return ResponseEntity.ok("Utilisateur supprimé avec succès et notification envoyée.");
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Bienvenue sur le dashboard Admin";
    }
    @GetMapping("/test-jwt")
    public String testJWT() {
        System.out.println("JWTUtil bean injected successfully!");
        return "Test JWTUtil";
    }






    @PutMapping("/update/{idU}")
    public ResponseEntity<?> updateUser(@PathVariable Long idU, @RequestBody UpdateUserRequest request) {
        System.out.println("🚀 Requête reçue pour update - ID: " + idU);
        System.out.println("👤 Utilisateur actuel : " + utilisateurRepository.findById(idU).orElse(null));
        Utilisateur utilisateur = utilisateurRepository.findById(idU)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // ✅ الاحتفاظ بكلمة المرور الحالية إذا لم يتم تغييرها
        String ancienMotDePasse = utilisateur.getMotPasse();
        boolean emailChanged = !utilisateur.getMail().equals(request.getMail());

        // ✅ تحديث البيانات المدخلة فقط
        if (request.getNom() != null) utilisateur.setNom(request.getNom());
        if (request.getPrenom() != null) utilisateur.setPrenom(request.getPrenom());
        if (request.getMail() != null) utilisateur.setMail(request.getMail());
        if (request.getRole() != null) utilisateur.setRole(request.getRole());
        if (request.getNumTel() != null) utilisateur.setNumTel(request.getNumTel());
        if (request.getVille() != null) utilisateur.setVille(request.getVille());
        if (request.getCodePostal() != null) utilisateur.setCodePostal(request.getCodePostal());
        if (request.getNomSociete() != null) utilisateur.setNomSociete(request.getNomSociete());
        if (request.getNumIdentificationEntreprise() != null) utilisateur.setNumIdentificationEntreprise(request.getNumIdentificationEntreprise());
        if (request.getContractActif() != null) utilisateur.setContractActif(request.getContractActif());
        if (request.getType() != null) utilisateur.setType(request.getType());


        // ✅ تحويل `String` إلى `Date`
        try {
            if (request.getDateSignatureContract() != null) {
                utilisateur.setDateSignatureContract(dateFormat.parse(request.getDateSignatureContract()));
            }
            if (request.getDateExpirationContract() != null) {
                utilisateur.setDateExpirationContract(dateFormat.parse(request.getDateExpirationContract()));
            }
            if (request.getDateEmbauche() != null) {
                utilisateur.setDateEmbauche(dateFormat.parse(request.getDateEmbauche()));
            }
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("❌ Erreur de format de date: " + e.getMessage());
        }

        if (request.getNumConduit() != null) utilisateur.setNumConduit(request.getNumConduit());
        if (request.getNumSecuritySocial() != null) utilisateur.setNumSecuritySocial(request.getNumSecuritySocial());

        // ✅ تحديث `PointVente` إذا تم إرساله
        if (request.getPointVente() != null) {
            PtVente ptVente = ptVenteRepository.findById(request.getPointVente())
                    .orElseThrow(() -> new RuntimeException("Point de vente non trouvé"));
            utilisateur.setPointVente(ptVente);
        }

        // ✅ تحديث كلمة المرور فقط إذا تم إرسال كلمة مرور جديدة
        if (request.getMotPasse() != null && !request.getMotPasse().isEmpty()) {
            utilisateur.setMotPasse(passwordEncoder.encode(request.getMotPasse()));
        } else {
            utilisateur.setMotPasse(ancienMotDePasse); // تبقى كلمة المرور كما هي
        }

        utilisateurRepository.save(utilisateur);

        // ✅ إرسال إيميل إذا تغير البريد الإلكتروني
        if (emailChanged) {
            try {
                String passwordToSend = request.getMotPasse() != null ? request.getMotPasse() : utilisateur.getMotPasse(); // Mot de passe wa9teli yetbadel mail
                emailService.envoyerEmailCreationUtilisateur(utilisateur, passwordToSend);
            } catch (Exception e) {
                return ResponseEntity.ok("✅ Utilisateur mis à jour, mais une erreur est survenue lors de l'envoi de l'email.");
            }
        }


        return ResponseEntity.ok("✅ Utilisateur mis à jour avec succès !");
    }
    @GetMapping("/liste")
    public ResponseEntity<List<Utilisateur>> getAllUsersExcludingAdmin() {
        List<Utilisateur> users = utilisateurService.getAllUsersExcludingAdmin();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/utilisateurs/filter")
    public ResponseEntity<List<Utilisateur>> filterUtilisateurs(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String numTel) {

        List<Utilisateur> utilisateurs;

        if (role != null) {
            utilisateurs = utilisateurRepository.findByRole(role);
        } else if (cin != null) {
            utilisateurs = utilisateurRepository.findByCinContaining(cin);
        } else if (nom != null) {
            utilisateurs = utilisateurRepository.findByNomContainingIgnoreCase(nom);
        } else if (prenom != null) {
            utilisateurs = utilisateurRepository.findByPrenomContainingIgnoreCase(prenom);
        } else if (mail != null) {
            utilisateurs = utilisateurRepository.findByMailContainingIgnoreCase(mail);
        } else if (numTel != null) {
            utilisateurs = utilisateurRepository.findByNumTelContaining(numTel);
        } else {
            utilisateurs = utilisateurRepository.findAll();
        }

        return ResponseEntity.ok(utilisateurs);
    }
    @GetMapping("/utilisateurs/summary")
    public ResponseEntity<List<UtilisateurSummaryDTO>> getUtilisateurSummary() {
        List<UtilisateurSummaryDTO> summaries = utilisateurRepository.findAll().stream()
                .map(u -> new UtilisateurSummaryDTO(
                        u.getCin(),
                        u.getNom(),
                        u.getPrenom(),
                        u.getRole(),
                        u.getMail(),
                        u.getNumTel()
                )).collect(Collectors.toList());

        return ResponseEntity.ok(summaries);
    }
    @GetMapping("/utilisateur/{idU}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long idU) {
        Utilisateur utilisateur = utilisateurRepository.findById(idU)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return ResponseEntity.ok(utilisateur);
    }
    @GetMapping("/demandes/attente")
    public ResponseEntity<List<ResponsablePointDeVenteDTO>> getDemandesEnAttente() {
        List<ResponsablePointDeVenteDTO> demandes = responsablePointVenteService.getDemandesEnAttente();
        return ResponseEntity.ok(demandes);
    }

    @PutMapping("/demande/{id}/accepter")
    public ResponseEntity<String> accepterDemande(@PathVariable Long id) {
        responsableService.accepterDemande(id);
        return ResponseEntity.ok("Responsable accepté et email envoyé !");
    }

    @PutMapping("/demande/refuser")
    public ResponseEntity<String> refuserDemande(@RequestBody RefusDTO refusDTO) {
        try {
            responsableService.refuserDemande(refusDTO.getResponsableId(), refusDTO.getRaison());
            return ResponseEntity.ok("Demande refusée et email envoyé.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }
    @GetMapping("/demandes/traitees")
    public ResponseEntity<List<ResponsablePointDeVenteDTO>> getDemandesTraitees() {
        List<ResponsablePointDeVenteDTO> demandes = responsablePointVenteService.getDemandesTraitees();
        return ResponseEntity.ok(demandes);
    }
    @GetMapping("/api/admin/contrat/{id}")
    public ResponseEntity<byte[]> getContratPDF(@PathVariable Long id) {
        ResponsablePointDeVente responsable = responsableRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsable non trouvé"));

        byte[] contratPdf = responsable.getContractSigned(); // adapte le nom selon ton entité

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("contrat.pdf").build());

        return new ResponseEntity<>(contratPdf, headers, HttpStatus.OK);
    }






}