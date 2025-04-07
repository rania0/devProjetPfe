package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AffectationRequest {
    private Long produitId;
    private Long fournisseurId;
    private Long sessionId;
    private Integer quantite;
    private LocalDate dateLimite;
    private String magazinePath;
    public String getMagazinePath() {
        return magazinePath;
    }

    public void setMagazinePath(String magazinePath) {
        this.magazinePath = magazinePath;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(Long fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(LocalDate dateLimite) {
        this.dateLimite = dateLimite;
    }
}

