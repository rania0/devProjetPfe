package com.example.backend.dto;

public class UtilisateurSummaryDTO {
    private String cin;
    private String nom;
    private String prenom;
    private String role;
    private String mail;
    private String numTel;

    public UtilisateurSummaryDTO(String cin, String nom, String prenom, String role, String mail, String numTel) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.mail = mail;
        this.numTel = numTel;
    }

    // Getters
    public String getCin() { return cin; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public String getMail() { return mail; }
    public String getNumTel() { return numTel; }
}
