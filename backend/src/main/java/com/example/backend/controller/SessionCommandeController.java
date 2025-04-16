package com.example.backend.controller;

import com.example.backend.dto.CommandeGroupeeDTO;
import com.example.backend.dto.SessionCommandeRequest;
import com.example.backend.entity.Commande;
import com.example.backend.entity.SessionCommande;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.CommandeRepository;
import com.example.backend.repository.SessionCommandeRepository;
import com.example.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session-commande")
public class SessionCommandeController {

    @Autowired
    private SessionCommandeRepository sessionCommandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    @PostMapping("/create")
    public SessionCommande createSession(@RequestBody SessionCommandeRequest request, Principal principal) {
        Utilisateur magasinier = utilisateurRepository.findByMail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Magasinier non trouvé"));

        SessionCommande session = new SessionCommande();
        session.setDateDebut(request.getDateDebut());
        session.setDateFin(request.getDateFin());
        session.setMagasinier(magasinier);

        return sessionCommandeRepository.save(session);
    }
    @GetMapping("/{sessionId}/groupes")
    public List<CommandeGroupeeDTO> getCommandesGroupées(@PathVariable Long sessionId) {
        // Récupérer toutes les commandes de cette session
        List<Commande> commandes = commandeRepository.findBySessionCommandeId(sessionId);

        // Grouper par produit
        Map<Long, CommandeGroupeeDTO> map = new HashMap<>();

        for (Commande commande : commandes) {
            Long produitId = commande.getProduitDisponible().getId();

            if (!map.containsKey(produitId)) {
                map.put(produitId, new CommandeGroupeeDTO(
                        produitId,
                        commande.getProduitDisponible().getNom(),
                        commande.getProduitDisponible().getTypeProduit(),
                        commande.getQuantite(),
                        commande.getProduitDisponible().getImageUrl()
                ));
            } else {
                CommandeGroupeeDTO dto = map.get(produitId);
                dto.setQuantiteTotale(dto.getQuantiteTotale() + commande.getQuantite());
            }
        }

        return new ArrayList<>(map.values());
    }
    @GetMapping("/last-closed")
    public ResponseEntity<Long> getLastClosedSessionId() {
        List<SessionCommande> sessions = sessionCommandeRepository.findAllByDateFinBeforeNowOrderByDateFinDesc();

        if (sessions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sessions.get(0).getId()); // le dernier clôturé
    }




}
