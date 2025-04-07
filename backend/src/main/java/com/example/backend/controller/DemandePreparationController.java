package com.example.backend.controller;

import java.io.File; // CORRECT import
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

import com.example.backend.dto.AffectationRequest;
import com.example.backend.dto.MagazineUploadResponse;
import com.example.backend.entity.*;
import com.example.backend.repository.DemandePreparationRepository;
import com.example.backend.repository.ProduitDisponibleCommandeRepository;
import com.example.backend.repository.SessionCommandeRepository;
import com.example.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/upload-magazine")
    public ResponseEntity<MagazineUploadResponse> uploadMagazine(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/magazines/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, file.getBytes());

            String fileUrl = "/uploads/magazines/" + fileName;
            return ResponseEntity.ok(new MagazineUploadResponse(fileName, fileUrl));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MagazineUploadResponse(null, "❌ Erreur lors de l'upload"));
        }
    }




    @PostMapping("/affecter")
    public ResponseEntity<?> affecterFournisseur(@RequestBody AffectationRequest request) {

        // 🔍 Récupération produit
        ProduitDisponibleCommande produit = produitDisponibleCommandeRepository.findById(request.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // 🔍 Récupération fournisseur
        Utilisateur fournisseur = utilisateurRepository.findById(request.getFournisseurId())
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        // 🔍 Récupération session
        SessionCommande session = sessionCommandeRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        // 📦 Création de la demande
        DemandePreparation demande = new DemandePreparation();
        demande.setProduit(produit);
        demande.setFournisseur(fournisseur);
        demande.setSession(session);
        demande.setQuantiteDemandee(request.getQuantite());
        demande.setStatut(StatutDemandePreparation.EN_ATTENTE);
        demande.setDateLimitePreparation(request.getDateLimite().atStartOfDay());

        // 📰 Cas particulier pour les magazines : ajouter le chemin du fichier PDF
        if ("MAGAZINE".equalsIgnoreCase(produit.getTypeProduit()) && request.getMagazinePath() != null) {
            demande.setMagazinePath(request.getMagazinePath());
        }

        demandePreparationRepository.save(demande);

        return ResponseEntity.ok("✅ Fournisseur affecté avec succès !");
    }


}

