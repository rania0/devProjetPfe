package com.example.backend.controller;

import com.example.backend.dto.RefusDTO;
import com.example.backend.dto.ResponsablePointDeVenteDTO;
import com.example.backend.entity.PtVente;
import com.example.backend.entity.ResponsablePointDeVente;
import com.example.backend.entity.StatutResponsable;
import com.example.backend.repository.PtVenteRepository;
import com.example.backend.repository.ResponsablePointDeVenteRepository;
import com.example.backend.service.EmailService;
import com.example.backend.service.ResponsablePointDeVenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/responsable")
@CrossOrigin("*")
public class ResponsablePointDeVenteController {

    @Autowired
    private PtVenteRepository ptVenteRepository;

    @Autowired
    private ResponsablePointDeVenteRepository responsableRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResponsablePointDeVenteService responsablePointVenteService;
    @Autowired
    private ResponsablePointDeVenteService responsableService;

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmerInscription(@RequestBody ResponsablePointDeVenteDTO dto) {
        Optional<PtVente> pointDeVenteOpt = ptVenteRepository.findById(dto.getPointDeVenteId());
        if (pointDeVenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Point de vente non trouvé.");
        }

        String signedFileName = "signed_contrat_" + dto.getNom() + "_" + dto.getPrenom() + ".pdf";
        Path signedPath = Paths.get("signed_contracts/" + signedFileName);
        byte[] signedBytes;

        try {
            signedBytes = Files.readAllBytes(signedPath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lecture du contrat signé : " + e.getMessage());
        }

        ResponsablePointDeVente responsable = ResponsablePointDeVente.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .motDePasse(passwordEncoder.encode(dto.getMotDePasse()))
                .rawPassword(dto.getMotDePasse())
                .telephone(dto.getTelephone())
                .cin(dto.getCin())
                .adresse(dto.getAdresse())
                .typeActivite(dto.getTypeActivite())
                .pointDeVente(pointDeVenteOpt.get())
                .status(StatutResponsable.EN_ATTENTE)
                .contractSigned(signedBytes)
                .build();

        responsableRepository.save(responsable);
        return ResponseEntity.ok("Inscription confirmée avec contrat signé enregistré !");
    }





}