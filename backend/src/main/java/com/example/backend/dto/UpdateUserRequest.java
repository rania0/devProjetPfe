package com.example.backend.dto;

public class UpdateUserRequest {

    private String nom;
    private String prenom;
    private String mail;
    private String role;
    private String motPasse;
    private String numTel;
    private String ville;
    private String codePostal;
    private String nomSociete;
    private String numIdentificationEntreprise;
    private Boolean contractActif;
    private String dateSignatureContract;
    private String dateExpirationContract;
    private String dateEmbauche;
    private String numConduit;
    private String numSecuritySocial;
    private Long pointVente;
    private String type;


    // ✅ Getters and Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNumTel() { return numTel; }
    public void setNumTel(String numTel) { this.numTel = numTel; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public String getNomSociete() { return nomSociete; }
    public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }

    public String getNumIdentificationEntreprise() { return numIdentificationEntreprise; }
    public void setNumIdentificationEntreprise(String numIdentificationEntreprise) { this.numIdentificationEntreprise = numIdentificationEntreprise; }

    public Boolean getContractActif() { return contractActif; }
    public void setContractActif(Boolean contractActif) { this.contractActif = contractActif; }

    public String getDateSignatureContract() { return dateSignatureContract; }
    public void setDateSignatureContract(String dateSignatureContract) { this.dateSignatureContract = dateSignatureContract; }

    public String getDateExpirationContract() { return dateExpirationContract; }
    public void setDateExpirationContract(String dateExpirationContract) { this.dateExpirationContract = dateExpirationContract; }

    public String getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getNumConduit() { return numConduit; }
    public void setNumConduit(String numConduit) { this.numConduit = numConduit; }

    public String getNumSecuritySocial() { return numSecuritySocial; }
    public void setNumSecuritySocial(String numSecuritySocial) { this.numSecuritySocial = numSecuritySocial; }

    public Long getPointVente() { return pointVente; }
    public void setPointVente(Long pointVente) { this.pointVente = pointVente; }

}
