package com.example.backend.controller;

import com.example.backend.entity.PtVente;
import com.example.backend.repository.PtVenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/point_vente")
public class PointDeVenteController {
    @Autowired
    private PtVenteRepository ptVenteRepository;

    @GetMapping("/commandes")
    public String commandes() {
        return "Bienvenue dans la gestion des commandes pour le point de vente";
    }
    @GetMapping("/all")
    public ResponseEntity<List<PtVente>> getAllPointsDeVente() {
        List<PtVente> pointsDeVente = ptVenteRepository.findAll();
        return ResponseEntity.ok(pointsDeVente);
    }
}

