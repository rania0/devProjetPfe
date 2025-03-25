package com.example.backend.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsablePointDeVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;


    @Column(nullable = false)
    private String telephone;

    @Column(unique = true, nullable = false)
    private String cin;

    @Column(nullable = false)
    private String adresse;

    @Transient
    private String rawPassword;

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    @Column(nullable = false)
    private String typeActivite;
    @Column(nullable = true)
    private String contractPath;
    @Lob
    @Column(name = "contract_signed", columnDefinition = "LONGBLOB")
    private byte[] contractSigned;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutResponsable status = StatutResponsable.EN_ATTENTE; // Statut par défaut

    @ManyToOne
    @JoinColumn(name = "point_de_vente_id", nullable = false)
    private PtVente pointDeVente;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public StatutResponsable getStatus() {
        return status;
    }

    public void setStatus(StatutResponsable status) {
        this.status = status;
    }

    public PtVente getPointDeVente() {
        return pointDeVente;
    }

    public void setPointDeVente(PtVente pointDeVente) {
        this.pointDeVente = pointDeVente;
    }

    public String getContractPath() {
        return contractPath;
    }

    public void setContractPath(String contractPath) {
        this.contractPath = contractPath;
    }

    public byte[] getContractSigned() {
        return contractSigned;
    }

    public void setContractSigned(byte[] contractSigned) {
        this.contractSigned = contractSigned;
    }
}
