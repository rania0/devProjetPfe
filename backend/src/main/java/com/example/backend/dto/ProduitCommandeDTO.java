package com.example.backend.dto;

import lombok.Data;

@Data
public class ProduitCommandeDTO {
    private Long produitId;
    private int quantite;

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}

