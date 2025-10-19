package com.fresh.cart.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "authentification")
public class Authentification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auth")
    private int idAuth;

    @OneToOne
    @JoinColumn(name = "id_utilisateur", referencedColumnName = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    @Column(name = "mot_de_passe", nullable = false, length = 255)
    private String motDePasse;

    @Column(name = "dernier_connexion", insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String dernierConnexion;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_compte", columnDefinition = "ENUM('ACTIF', 'INACTIF', 'SUSPENDU')", nullable = false)
    private StatutCompte statutCompte = StatutCompte.ACTIF;

    @Column(name = "email_verifie", nullable = false)
    private boolean emailVerifie = false;

    @Column(name = "token", unique = true)
    private String token;

    public int getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(int idAuth) {
        this.idAuth = idAuth;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getDernierConnexion() {
        return dernierConnexion;
    }

    public void setDernierConnexion(String dernierConnexion) {
        this.dernierConnexion = dernierConnexion;
    }

    public StatutCompte getStatutCompte() {
        return statutCompte;
    }

    public void setStatutCompte(StatutCompte statutCompte) {
        this.statutCompte = statutCompte;
    }

    public boolean isEmailVerifie() {
        return emailVerifie;
    }

    public void setEmailVerifie(boolean emailVerifie) {
        this.emailVerifie = emailVerifie;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public enum StatutCompte {
        ACTIF, INACTIF, SUSPENDU
    }
}
