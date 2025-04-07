package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandePreparation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produit demandé
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private ProduitDisponibleCommande produit;

    // Quantité demandée à ce fournisseur
    @Column(nullable = false)
    private int quantiteDemandee;

    // Fournisseur sélectionné pour préparer ce produit
    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Utilisateur fournisseur;

    // Session de commande liée
    @ManyToOne
    @JoinColumn(name = "session_commande_id", nullable = false)
    private SessionCommande session;

    // Statut de la demande (EN_ATTENTE, ACCEPTEE, REFUSEE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemandePreparation statut = StatutDemandePreparation.EN_ATTENTE;

    // Date limite fixée par le magasinier pour la réponse
    @Column(nullable = false)
    private LocalDateTime dateLimitePreparation;
    @Column(name = "magazine_path")
    private String magazinePath;
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_commande")
    private EtatCommande etatCommande = EtatCommande.EN_PREPARATION;

    public EtatCommande getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(EtatCommande etatCommande) {
        this.etatCommande = etatCommande;
    }

    public String getMagazinePath() {
        return magazinePath;
    }

    public void setMagazinePath(String magazinePath) {
        this.magazinePath = magazinePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProduitDisponibleCommande getProduit() {
        return produit;
    }

    public void setProduit(ProduitDisponibleCommande produit) {
        this.produit = produit;
    }

    public int getQuantiteDemandee() {
        return quantiteDemandee;
    }

    public void setQuantiteDemandee(int quantiteDemandee) {
        this.quantiteDemandee = quantiteDemandee;
    }

    public Utilisateur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Utilisateur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public SessionCommande getSession() {
        return session;
    }

    public void setSession(SessionCommande session) {
        this.session = session;
    }

    public StatutDemandePreparation getStatut() {
        return statut;
    }

    public void setStatut(StatutDemandePreparation statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateLimitePreparation() {
        return dateLimitePreparation;
    }

    public void setDateLimitePreparation(LocalDateTime dateLimitePreparation) {
        this.dateLimitePreparation = dateLimitePreparation;
    }
}