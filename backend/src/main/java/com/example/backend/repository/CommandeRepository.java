package com.example.backend.repository;

import com.example.backend.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByResponsableId(Long responsableId);
    List<Commande> findBySessionCommandeId(Long sessionId);
    @Query("SELECT c.produitDisponible.id, SUM(c.quantite) " +
            "FROM Commande c " +
            "WHERE c.sessionCommande.id = :sessionId " +
            "GROUP BY c.produitDisponible.id")
    List<Object[]> findQuantiteTotaleParProduitPourSession(@Param("sessionId") Long sessionId);


}
