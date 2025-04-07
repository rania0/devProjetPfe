package com.example.backend.dto;

public class CommandeGroupeeDTO {

    private Long produitId;
    private String nomProduit;
    private String typeProduit;
    private int quantiteTotale;
    private String imageUrl;

    public CommandeGroupeeDTO() {}

    public CommandeGroupeeDTO(Long produitId, String nomProduit, String typeProduit, int quantiteTotale, String imageUrl) {
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.typeProduit = typeProduit;
        this.quantiteTotale = quantiteTotale;
        this.imageUrl = imageUrl;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public int getQuantiteTotale() {
        return quantiteTotale;
    }

    public void setQuantiteTotale(int quantiteTotale) {
        this.quantiteTotale = quantiteTotale;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
