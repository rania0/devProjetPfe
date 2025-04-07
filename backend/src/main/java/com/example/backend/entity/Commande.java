package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantite;

    private LocalDateTime dateCommande;

    @Enumerated(EnumType.STRING)
    private TypeCommande typeCommande;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsablePointDeVente responsable;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionCommande sessionCommande;
    @ManyToOne
    @JoinColumn(name = "produit_disponible_id")
    private ProduitDisponibleCommande produitDisponible;

    public ProduitDisponibleCommande getProduitDisponible() {
        return produitDisponible;
    }

    public void setProduitDisponible(ProduitDisponibleCommande produitDisponible) {
        this.produitDisponible = produitDisponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public TypeCommande getTypeCommande() {
        return typeCommande;
    }

    public void setTypeCommande(TypeCommande typeCommande) {
        this.typeCommande = typeCommande;
    }

    public ResponsablePointDeVente getResponsable() {
        return responsable;
    }

    public void setResponsable(ResponsablePointDeVente responsable) {
        this.responsable = responsable;
    }

    public SessionCommande getSessionCommande() {
        return sessionCommande;
    }

    public void setSessionCommande(SessionCommande sessionCommande) {
        this.sessionCommande = sessionCommande;
    }
}
