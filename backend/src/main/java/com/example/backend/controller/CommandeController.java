package com.example.backend.controller;

import com.example.backend.dto.CommandeRequest;
import com.example.backend.entity.*;
import com.example.backend.repository.CommandeRepository;
import com.example.backend.repository.ProduitDisponibleCommandeRepository;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.repository.SessionCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commande")
public class CommandeController {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ResponsablePointDeVenteRepository responsableRepo;

    @Autowired
    private SessionCommandeRepository sessionRepo;
    @Autowired
    private ProduitDisponibleCommandeRepository produitDisponibleCommandeRepository;


    @PostMapping("/passer")
    public Commande passerCommande(@RequestBody CommandeRequest request, Principal principal) {
        ResponsablePointDeVente responsable = responsableRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        LocalDateTime now = LocalDateTime.now();
        List<SessionCommande> sessionsActives = sessionRepo.findByDateDebutBeforeAndDateFinAfter(now, now);

        if (sessionsActives.isEmpty()) {
            throw new RuntimeException("⛔ Aucune session de commande active actuellement.");
        }

        // 💡 Récupérer le produit choisi
        ProduitDisponibleCommande produit = produitDisponibleCommandeRepository
                .findById(request.getProduitDisponibleId())
                .orElseThrow(() -> new RuntimeException("Produit sélectionné introuvable."));

        Commande commande = new Commande();
        commande.setResponsable(responsable);
        commande.setQuantite(request.getQuantite());
        commande.setDateCommande(now);
        commande.setSessionCommande(sessionsActives.get(0));
        commande.setProduitDisponible(produit); // 💥 Liaison au modèle choisi

        return commandeRepository.save(commande);
    }

    @GetMapping("/mes-commandes")
    public List<Commande> getMesCommandes(Principal principal) {
        ResponsablePointDeVente responsable = responsableRepo.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Responsable non trouvé"));

        return commandeRepository.findByResponsableId(responsable.getId());
    }

}
