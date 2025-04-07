package com.example.backend.controller;

import com.example.backend.entity.DemandePreparation;
import com.example.backend.entity.EtatCommande;
import com.example.backend.entity.StatutDemandePreparation;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.DemandePreparationRepository;
import com.example.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseur")
public class FournisseurController {

    @Autowired
    private DemandePreparationRepository demandePreparationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // ✅ Voir toutes les demandes assignées à ce fournisseur
    @GetMapping("/mes-demandes")
    public ResponseEntity<?> getDemandesPourFournisseurConnecte(Authentication authentication) {
        String email = authentication.getName();
        Utilisateur fournisseur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        List<DemandePreparation> toutesDemandes = demandePreparationRepository
                .findByFournisseur_IdU(fournisseur.getIdU());

        String typeFournisseur = fournisseur.getType(); // Exemple : "fournisseur papier thermique" ou "imprimeur"

        // 🔍 Filtrage des demandes selon le type
        List<DemandePreparation> demandesFiltrees = toutesDemandes.stream()
                .filter(d -> {
                    if ("fournisseur papier thermique".equalsIgnoreCase(typeFournisseur)) {
                        return "PAPIER_THERMIQUE".equalsIgnoreCase(d.getProduit().getTypeProduit());
                    } else if ("imprimeur".equalsIgnoreCase(typeFournisseur)) {
                        return "MAGAZINE".equalsIgnoreCase(d.getProduit().getTypeProduit());
                    }
                    return false;
                })
                .toList();

        return ResponseEntity.ok(demandesFiltrees);
    }


    // ✅ Accepter ou refuser une demande
    @PostMapping("/traiter")
    public ResponseEntity<?> traiterDemande(@RequestParam Long demandeId,
                                            @RequestParam boolean accepter) {
        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        demande.setStatut(accepter ? StatutDemandePreparation.ACCEPTEE : StatutDemandePreparation.REFUSEE);
        demandePreparationRepository.save(demande);

        return ResponseEntity.ok(accepter ? "✅ Demande acceptée avec succès." : "❌ Demande refusée.");
    }
    @PostMapping("/changer-etat")
    public ResponseEntity<?> changerEtatCommande(@RequestParam Long demandeId,
                                                 @RequestParam EtatCommande nouvelEtat) {
        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        demande.setEtatCommande(nouvelEtat);
        demandePreparationRepository.save(demande);

        return ResponseEntity.ok("✅ État mis à jour vers : " + nouvelEtat);
    }

}
