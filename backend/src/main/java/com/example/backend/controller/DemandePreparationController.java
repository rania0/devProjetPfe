package com.example.backend.controller;

import java.io.File; // CORRECT import
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.backend.dto.AffectationRequest;
import com.example.backend.dto.MagazineUploadResponse;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/preparation")
public class DemandePreparationController {

    @Autowired
    private DemandePreparationRepository demandePreparationRepository;

    @Autowired
    private ProduitDisponibleCommandeRepository produitDisponibleCommandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private SessionCommandeRepository sessionCommandeRepository;
    @Autowired
    private CommandeRepository commandeRepository;

@PostMapping("/upload-magazine")
public ResponseEntity<String> uploadMagazinePDF(@RequestParam("file") MultipartFile file,
                                                @RequestParam("produitId") Long produitId,
                                                @RequestParam("sessionId") Long sessionId) {
    try {
        ProduitDisponibleCommande produit = produitDisponibleCommandeRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        SessionCommande session = sessionCommandeRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        DemandePreparation demande = demandePreparationRepository
                .findByProduit_IdAndSession_Id(produitId, sessionId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable pour ce produit et session"));

        demande.setPdfMagazine(file.getBytes()); // 🧠 Ajoute le fichier dans la base
        demandePreparationRepository.save(demande);

        return ResponseEntity.ok("✅ PDF bien enregistré dans la base de données.");
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la lecture du fichier PDF");
    }
}


    @PostMapping("/affectation-auto")
    public ResponseEntity<?> affectationAutomatique(@RequestParam Long sessionId, @RequestParam String dateLimite) {
        try {
            LocalDateTime limite = LocalDate.parse(dateLimite).atStartOfDay();

            List<Object[]> quantites = commandeRepository.findQuantiteTotaleParProduitPourSession(sessionId);

            for (Object[] row : quantites) {
                Long produitId = (Long) row[0];
                Integer quantiteTotale = ((Number) row[1]).intValue();

                ProduitDisponibleCommande produit = produitDisponibleCommandeRepository.findById(produitId)
                        .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

                // 🟡 Créer une seule demande sans fournisseur
                boolean existe = demandePreparationRepository
                        .findBySession_IdAndProduit_Id(sessionId, produitId)
                        .isPresent();

                if (!existe) {
                    DemandePreparation demande = new DemandePreparation();
                    demande.setProduit(produit);
                    demande.setSessionCommande(sessionCommandeRepository.findById(sessionId)
                            .orElseThrow(() -> new RuntimeException("Session non trouvée")));
                    demande.setQuantiteDemandee(quantiteTotale);
                    demande.setStatut(StatutDemandePreparation.EN_ATTENTE);
                    demande.setDateLimitePreparation(limite);

                    demandePreparationRepository.save(demande);
                }
            }

            return ResponseEntity.ok("✅ Affectation automatique enregistrée (sans fournisseur).");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erreur durant l’affectation: " + e.getMessage());
        }
    }

@GetMapping("/non-affectees")
public ResponseEntity<?> getDemandesNonAffectees(@RequestParam Long sessionId) {
    List<DemandePreparation> demandes = demandePreparationRepository
            .findBySession_IdAndFournisseurIsNull(sessionId);
    return ResponseEntity.ok(demandes);
}

    @PostMapping("/relancer")
    public ResponseEntity<?> relancerAffectationPourProduit(
            @RequestParam Long produitId,
            @RequestParam Long sessionId,
            @RequestParam String dateLimite // yyyy-MM-dd
    ) {
        try {
            LocalDateTime limite = LocalDate.parse(dateLimite).atStartOfDay();

            ProduitDisponibleCommande produit = produitDisponibleCommandeRepository.findById(produitId)
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            SessionCommande session = sessionCommandeRepository.findById(sessionId)
                    .orElseThrow(() -> new RuntimeException("Session non trouvée"));

            // ✅ Récupérer une ancienne demande (si elle existe)
            Optional<DemandePreparation> ancienneDemandeOpt = demandePreparationRepository
                    .findFirstByProduit_IdAndSession_IdOrderByIdDesc(produitId, sessionId);

            int nbAjoutes = 0;
            String type = produit.getTypeProduit();
            List<Utilisateur> fournisseurs = utilisateurRepository.findByRoleAndType("fournisseur",
                    type.equalsIgnoreCase("MAGAZINE") ? "imprimeur" : "fournisseur papier thermique");

            for (Utilisateur fournisseur : fournisseurs) {
                boolean existe = demandePreparationRepository
                        .findByFournisseur_IdUAndSession_IdAndProduit_Id(fournisseur.getIdU(), sessionId, produitId)
                        .isPresent();
                if (!existe) {
                    DemandePreparation demande = new DemandePreparation();
                    demande.setProduit(produit);
                    demande.setFournisseur(null); // Attente de l'acceptation
                    demande.setSessionCommande(session);
                    demande.setStatut(StatutDemandePreparation.EN_ATTENTE);
                    demande.setEtatCommande(EtatCommande.EN_PREPARATION);
                    demande.setDateLimitePreparation(limite);

                    // Copier quantite et PDF si existant
                    if (ancienneDemandeOpt.isPresent()) {
                        DemandePreparation ancienne = ancienneDemandeOpt.get();
                        demande.setQuantiteDemandee(ancienne.getQuantiteDemandee());
                        demande.setPdfMagazine(ancienne.getPdfMagazine());
                    } else {
                        demande.setQuantiteDemandee(0); // par défaut
                    }

                    demandePreparationRepository.save(demande);
                    nbAjoutes++;
                }
            }

            return ResponseEntity.ok("✅ Relance effectuée pour produit " + produitId +
                    " → " + nbAjoutes + " fournisseurs disponibles.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erreur relance : " + e.getMessage());
        }
    }

    @GetMapping("/demande-affectees")
    public ResponseEntity<?> getDemandesAffecteesDerniereSession() {
        List<SessionCommande> sessions = sessionCommandeRepository.findAllByDateFinBeforeNowOrderByDateFinDesc();
        if (sessions.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Aucune session clôturée trouvée.");
        }
        SessionCommande derniereSession = sessions.get(0);

        List<DemandePreparation> affectees = demandePreparationRepository
                .findByFournisseurIsNotNullAndSession_Id(derniereSession.getId());

        return ResponseEntity.ok(affectees);
    }
    @PutMapping("/confirmer-livraison/{idDemande}")
    public ResponseEntity<?> confirmerLivraison(@PathVariable Long idDemande) {
        DemandePreparation demande = demandePreparationRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (!EtatCommande.EN_ROUTE.equals(demande.getEtatCommande())) {
            return ResponseEntity.badRequest().body("❌ La commande n’est pas encore en route !");
        }

        demande.setEtatCommande(EtatCommande.LIVREE);
        demandePreparationRepository.save(demande);

        return ResponseEntity.ok("✅ Commande confirmée comme livrée.");
    }








}

