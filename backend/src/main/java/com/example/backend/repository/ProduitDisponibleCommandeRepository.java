package com.example.backend.repository;

import com.example.backend.entity.ProduitDisponibleCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitDisponibleCommandeRepository extends JpaRepository<ProduitDisponibleCommande, Long> {
    List<ProduitDisponibleCommande> findByTypeProduit(String typeProduit);
}
