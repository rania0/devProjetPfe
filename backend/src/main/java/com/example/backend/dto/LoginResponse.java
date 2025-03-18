package com.example.backend.dto;



public class LoginResponse {

    private Long idU;
    private String nom;
    private String prenom;
    private String role;
    private String mail;
    private String Token;
    private String refreshToken;// هذا هو التوكن الجديد

    public LoginResponse(Long idU, String nom, String prenom,String mail, String role, String  Token , String refreshToken) {
        this.idU = idU;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.role = role;
        this.Token = Token;
        this.refreshToken = refreshToken;
    }

    public Long getIdU() {
        return idU;
    }

    public void setIdU(Long idU) {
        this.idU = idU;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
}

