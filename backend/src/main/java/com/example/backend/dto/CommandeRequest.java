package com.example.backend.dto;

import com.example.backend.entity.TypeCommande;

public class CommandeRequest {
    private int quantite;
    private TypeCommande typeCommande;
    private Long produitDisponibleId;

    public Long getProduitDisponibleId() {
        return produitDisponibleId;
    }

    public void setProduitDisponibleId(Long produitDisponibleId) {
        this.produitDisponibleId = produitDisponibleId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public TypeCommande getTypeCommande() {
        return typeCommande;
    }

    public void setTypeCommande(TypeCommande typeCommande) {
        this.typeCommande = typeCommande;
    }
}
