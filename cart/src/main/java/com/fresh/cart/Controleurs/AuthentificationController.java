package com.fresh.cart.Controleurs;

import com.fresh.cart.Entities.Authentification;
import com.fresh.cart.Entities.ConnexionRequest;
import com.fresh.cart.Entities.InscriptionRequest;
import com.fresh.cart.Entities.Utilisateur;
import com.fresh.cart.Services.AuthentificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final AuthentificationService authentificationService;

    @Autowired
    public AuthentificationController(AuthentificationService authentificationService) {
        this.authentificationService = authentificationService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscrireUtilisateur(@RequestParam("nom") String nom,
                                              @RequestParam("prenom") String prenom,
                                              @RequestParam("courriel") String courriel,
                                              @RequestParam("role") String role,
                                              @RequestParam("telephone") String telephone,
                                              @RequestParam("motDePasse") String motPasse
                                                ) throws MessagingException {

        Utilisateur utilisateur = new Utilisateur();
        Authentification authentification = new Authentification();

//        if (nom.isEmpty() || prenom.isEmpty() || courriel.isEmpty() || role.isEmpty()||
//                telephone.isEmpty() || motPasse.isEmpty()) {
//            return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
//        } else {
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setCourriel(courriel);
            utilisateur.setTelephone(telephone);
            utilisateur.setRole(role);

            authentificationService.inscrireUtilisateur(utilisateur, motPasse);
            return new ResponseEntity<>("succes", HttpStatus.OK);
//        }
    }
    @GetMapping("/verify")
    public String verifierEmail(@RequestParam("token") String token) {
        boolean verified = authentificationService.verifierEmail(token);
        return verified ? "Compte vérifié avec succès !" : "Échec de la vérification.";
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connexionUtilisateur(@RequestBody ConnexionRequest request) {
        try {
            Utilisateur utilisateur = authentificationService.connecterUtilisateur(
                    request.getCourriel(), request.getMotDePasse()
            );
            return ResponseEntity.ok(utilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/mot-de-passe-oublie")
    public ResponseEntity<?> motDePasseOublie(@RequestParam("courriel") String courriel) {
        try {
            authentificationService.motDePasseOublie(courriel);
            return new ResponseEntity<>("succes", HttpStatus.OK);
            //return ResponseEntity.ok("Un e-mail de réinitialisation a été envoyé.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/reinitialiser-mot-de-passe")
    public ResponseEntity<?> reinitialiserMotDePasse(
            @RequestParam("token") String token,
            @RequestParam("nouveauMotDePasse") String nouveauMotDePasse) {
        try {
            authentificationService.reinitialiserMotDePasse(token, nouveauMotDePasse);
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/deconnexion")
    public ResponseEntity<?> deconnexionUtilisateur(@RequestBody Map<String, String> request) {
        try {
            authentificationService.deconnecterUtilisateur(request.get("courriel"));
            return ResponseEntity.ok("Déconnexion réussie.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}