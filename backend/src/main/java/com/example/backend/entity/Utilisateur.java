package com.example.backend.entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idU;

    private String role;
    private String nomSociete;
    private String numIdentificationEntreprise;
    private Boolean contractActif;
    private Date dateSignatureContract;
    private Date dateExpirationContract;

    private String nom;
    private String prenom;
    private String numSecuritySocial;
    private String cin;
    private String numTel;
    private String numConduit;
    private String mail;
    private String motPasse;
    private String adress;
    private String ville;
    private String codePostal;
    private Date dateEmbauche;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "idPt", referencedColumnName = "idP")
    private PtVente pointVente;  // relation vers point de vente

    // Getters and Setters
    public Long getIdPt() {
        return pointVente != null ? pointVente.getIdP() : null;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void setIdPt(Long idPt) {
        if (this.pointVente == null) {
            this.pointVente = new PtVente();
        }
        this.pointVente.setIdP(idPt);
    }

    public Long getIdU() {
        return idU;
    }

    public void setIdU(Long idU) {
        this.idU = idU;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getNumIdentificationEntreprise() {
        return numIdentificationEntreprise;
    }

    public void setNumIdentificationEntreprise(String numIdentificationEntreprise) {
        this.numIdentificationEntreprise = numIdentificationEntreprise;
    }

    public Boolean getContractActif() {
        return contractActif;
    }

    public void setContractActif(Boolean contractActif) {
        this.contractActif = contractActif;
    }

    public Date getDateSignatureContract() {
        return dateSignatureContract;
    }

    public void setDateSignatureContract(Date dateSignatureContract) {
        this.dateSignatureContract = dateSignatureContract;
    }

    public Date getDateExpirationContract() {
        return dateExpirationContract;
    }

    public void setDateExpirationContract(Date dateExpirationContract) {
        this.dateExpirationContract = dateExpirationContract;
    }

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

    public String getNumSecuritySocial() {
        return numSecuritySocial;
    }

    public void setNumSecuritySocial(String numSecuritySocial) {
        this.numSecuritySocial = numSecuritySocial;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getNumConduit() {
        return numConduit;
    }

    public void setNumConduit(String numConduit) {
        this.numConduit = numConduit;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public PtVente getPointVente() {
        return pointVente;
    }

    public void setPointVente(PtVente pointVente) {
        this.pointVente = pointVente;
    }
}