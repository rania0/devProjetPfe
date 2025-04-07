package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandePreparationProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produit concerné (modèle commandé)
    @ManyToOne
    @JoinColumn(name = "produit_disponible_id", nullable = false)
    private ProduitDisponibleCommande produit;

    // Le fournisseur sélectionné
    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Utilisateur fournisseur;

    private int quantiteTotale;

    private LocalDateTime dateEnvoi;

    private LocalDateTime dateLimitePreparation;

    @Enumerated(EnumType.STRING)
    private StatutPreparation statut = StatutPreparation.EN_ATTENTE;

    private String commentaireFournisseur; // optionnel, utilisé en cas de refus

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProduitDisponibleCommande getProduit() {
        return produit;
    }

    public void setProduit(ProduitDisponibleCommande produit) {
        this.produit = produit;
    }

    public Utilisateur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Utilisateur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public int getQuantiteTotale() {
        return quantiteTotale;
    }

    public void setQuantiteTotale(int quantiteTotale) {
        this.quantiteTotale = quantiteTotale;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public LocalDateTime getDateLimitePreparation() {
        return dateLimitePreparation;
    }

    public void setDateLimitePreparation(LocalDateTime dateLimitePreparation) {
        this.dateLimitePreparation = dateLimitePreparation;
    }

    public StatutPreparation getStatut() {
        return statut;
    }

    public void setStatut(StatutPreparation statut) {
        this.statut = statut;
    }

    public String getCommentaireFournisseur() {
        return commentaireFournisseur;
    }

    public void setCommentaireFournisseur(String commentaireFournisseur) {
        this.commentaireFournisseur = commentaireFournisseur;
    }
}
