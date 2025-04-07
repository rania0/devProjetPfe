package com.example.backend.service;

import com.example.backend.dto.SessionCommandeRequest;
import com.example.backend.entity.SessionCommande;
import com.example.backend.entity.Utilisateur;
import com.example.backend.repository.SessionCommandeRepository;
import com.example.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class SessionCommandeService {

    @Autowired
    private SessionCommandeRepository sessionCommandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public SessionCommande createSession(SessionCommandeRequest request, Principal principal) {
        Utilisateur magasinier = utilisateurRepository.findByMail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Magasinier non trouvé"));

        LocalDateTime debut = request.getDateDebut().withSecond(0).withNano(0);
        LocalDateTime fin = request.getDateFin().withSecond(0).withNano(0);

        SessionCommande session = new SessionCommande();
        session.setDateDebut(debut);
        session.setDateFin(fin);
        session.setMagasinier(magasinier);

        return sessionCommandeRepository.save(session);
    }
}
