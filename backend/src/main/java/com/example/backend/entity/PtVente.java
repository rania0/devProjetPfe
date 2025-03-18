package com.example.backend.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "pt_vente")
public class PtVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idP;

    private String nomP;
    private String adressMap;

    // Getters and Setters
    public Long getIdP() { return idP; }
    public void setIdP(Long idP) { this.idP = idP; }

    public String getNomP() { return nomP; }
    public void setNomP(String nomP) { this.nomP = nomP; }

    public String getAdressMap() { return adressMap; }
    public void setAdressMap(String adressMap) { this.adressMap = adressMap; }
}