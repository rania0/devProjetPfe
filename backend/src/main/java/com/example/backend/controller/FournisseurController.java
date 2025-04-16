package com.example.backend.controller;

import com.example.backend.entity.*;
import com.example.backend.repository.DemandePreparationRepository;
import com.example.backend.repository.SessionCommandeRepository;
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
    @Autowired
    private SessionCommandeRepository sessionCommandeRepository;

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
//    @PostMapping("/traiter")
//    public ResponseEntity<?> traiterDemande(@RequestParam Long demandeId,
//                                            @RequestParam boolean accepter) {
//        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
//                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
//
//        demande.setStatut(accepter ? StatutDemandePreparation.ACCEPTEE : StatutDemandePreparation.REFUSEE);
//        demandePreparationRepository.save(demande);
//
//        return ResponseEntity.ok(accepter ? "✅ Demande acceptée avec succès." : "❌ Demande refusée.");
//    }
//    @PostMapping("/changer-etat")
//    public ResponseEntity<?> changerEtatCommande(@RequestParam Long demandeId,
//                                                 @RequestParam EtatCommande nouvelEtat) {
//        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
//                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
//
//        demande.setEtatCommande(nouvelEtat);
//        demandePreparationRepository.save(demande);
//
//        return ResponseEntity.ok("✅ État mis à jour vers : " + nouvelEtat);
//    }
    @PostMapping("/traiter")
    public ResponseEntity<?> traiterDemande(@RequestParam Long demandeId,
                                            @RequestParam boolean accepter,
                                            Authentication authentication) {
        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (accepter) {
            String email = authentication.getName();
            Utilisateur fournisseur = utilisateurRepository.findByMail(email)
                    .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

            demande.setStatut(StatutDemandePreparation.ACCEPTEE);
            demande.setFournisseur(fournisseur); // ✅ fixer le fournisseur maintenant
        } else {
            demande.setStatut(StatutDemandePreparation.REFUSEE);
        }

        demandePreparationRepository.save(demande);

        return ResponseEntity.ok(accepter ? "✅ Demande acceptée avec succès." : "❌ Demande refusée.");
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNouvellesDemandes(Authentication authentication) {
        String email = authentication.getName();
        Utilisateur fournisseur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        String typeFournisseur = fournisseur.getType(); // "imprimeur" ou "fournisseur papier thermique"

        List<DemandePreparation> demandes = demandePreparationRepository.findByFournisseurIsNullAndStatut(StatutDemandePreparation.EN_ATTENTE);

        // 🔎 On filtre seulement les demandes compatibles avec le type du fournisseur
        List<DemandePreparation> demandesCompatibles = demandes.stream()
                .filter(d -> {
                    if ("imprimeur".equalsIgnoreCase(typeFournisseur)) {
                        return "MAGAZINE".equalsIgnoreCase(d.getProduit().getTypeProduit());
                    } else if ("fournisseur papier thermique".equalsIgnoreCase(typeFournisseur)) {
                        return "PAPIER_THERMIQUE".equalsIgnoreCase(d.getProduit().getTypeProduit());
                    }
                    return false;
                })
                .toList();

        return ResponseEntity.ok(demandesCompatibles);
    }
//    @GetMapping("/mes-demandes-traitees")
//    public ResponseEntity<?> getDemandesAccepteesEtEnPreparation(Authentication authentication) {
//        String email = authentication.getName();
//        Utilisateur fournisseur = utilisateurRepository.findByMail(email)
//                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));
//
//        List<DemandePreparation> demandes = demandePreparationRepository
//                .findByFournisseur_IdUAndStatutAndEtatCommande(
//                        fournisseur.getIdU(),
//                        StatutDemandePreparation.ACCEPTEE,
//                        EtatCommande.EN_PREPARATION
//                );
//
//        return ResponseEntity.ok(demandes);
//    }
@GetMapping("/mes-demandes-traitees")
public ResponseEntity<?> getDemandesAccepteesPourDerniereSession(Authentication authentication) {
    String email = authentication.getName();
    Utilisateur fournisseur = utilisateurRepository.findByMail(email)
            .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

    // ✅ Récupérer la dernière session clôturée
    List<SessionCommande> sessions = sessionCommandeRepository.findAllByDateFinBeforeNowOrderByDateFinDesc();
    if (sessions.isEmpty()) {
        return ResponseEntity.badRequest().body("❌ Aucune session clôturée trouvée.");
    }
    SessionCommande derniereSession = sessions.get(0); // Prend la dernière

    // ✅ Chercher les demandes liées à cette session et à ce fournisseur
    List<DemandePreparation> demandes = demandePreparationRepository
            .findByFournisseur_IdUAndStatutAndSession_Id(
                    fournisseur.getIdU(),
                    StatutDemandePreparation.ACCEPTEE,
                    derniereSession.getId()
            );

    return ResponseEntity.ok(demandes);
}

    @PostMapping("/changer-etat")
    public ResponseEntity<?> changerEtatCommande(@RequestParam Long demandeId,
                                                 @RequestParam EtatCommande nouvelEtat,
                                                 Authentication authentication) {
        String email = authentication.getName();
        Utilisateur fournisseur = utilisateurRepository.findByMail(email)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));

        DemandePreparation demande = demandePreparationRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // ✅ Vérifie que c’est bien sa propre demande
        if (!demande.getFournisseur().getIdU().equals(fournisseur.getIdU())) {
            return ResponseEntity.status(403).body("⛔️ Vous n'êtes pas autorisé à modifier cette demande.");
        }

        demande.setEtatCommande(nouvelEtat);
        demandePreparationRepository.save(demande);

        return ResponseEntity.ok("✅ État mis à jour vers : " + nouvelEtat);
    }



}
