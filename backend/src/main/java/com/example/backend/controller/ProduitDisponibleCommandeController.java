package com.example.backend.controller;

import com.example.backend.entity.ProduitDisponibleCommande;
import com.example.backend.repository.ProduitDisponibleCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class ProduitDisponibleCommandeController {

    @Autowired
    private ProduitDisponibleCommandeRepository produitRepo;

    // ✅ Ajouter un modèle (ex : depuis Postman)
    @PostMapping("/add")
    public ProduitDisponibleCommande ajouterProduit(@RequestBody ProduitDisponibleCommande produit) {
        return produitRepo.save(produit);
    }

    // ✅ Récupérer tous les modèles disponibles
    @GetMapping("/all")
    public List<ProduitDisponibleCommande> getAll() {
        return produitRepo.findAll();
    }

    // ✅ Récupérer les modèles par type
    @GetMapping("/type/{typeProduit}")
    public List<ProduitDisponibleCommande> getByType(@PathVariable String typeProduit) {
        return produitRepo.findByTypeProduit(typeProduit);
    }
}
