package com.example.backend.controller;

import com.example.backend.entity.ProduitDisponibleCommande;
import com.example.backend.repository.ProduitDisponibleCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

    @Autowired
    private ProduitDisponibleCommandeRepository produitRepo;

    @GetMapping
    public List<ProduitDisponibleCommande> getAll() {
        return produitRepo.findAll();
    }

    @GetMapping("/{type}")
    public List<ProduitDisponibleCommande> getByType(@PathVariable String type) {
        return produitRepo.findByTypeProduit(type);
    }
}
