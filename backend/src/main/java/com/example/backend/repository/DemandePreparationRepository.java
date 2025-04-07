package com.example.backend.repository;

import com.example.backend.entity.DemandePreparation;
import com.example.backend.entity.StatutDemandePreparation;
import com.example.backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemandePreparationRepository extends JpaRepository<DemandePreparation, Long> {

    // ✅ Récupérer les demandes par fournisseur
    List<DemandePreparation> findByFournisseur_IdU(Long fournisseurId);

    // ✅ Récupérer les demandes d’une session donnée
    List<DemandePreparation> findBySession_Id(Long sessionId);

    // ✅ Récupérer une demande spécifique
    Optional<DemandePreparation> findBySession_IdAndProduit_Id(Long sessionId, Long produitId);

    // ✅ Récupérer toutes les demandes encore en attente
    List<DemandePreparation> findByStatut(StatutDemandePreparation statut);

    // ✅ Vérifier si fournisseur a déjà une affectation
    Optional<DemandePreparation> findByFournisseur_IdUAndSession_IdAndProduit_Id(Long fournisseurId, Long sessionId, Long produitId);
    List<DemandePreparation> findByProduit_TypeProduitAndStatut(String typeProduit, StatutDemandePreparation statut);
    List<DemandePreparation> findByProduit_TypeProduitAndSession_IdAndStatut(String typeProduit, Long sessionId, StatutDemandePreparation statut);


}