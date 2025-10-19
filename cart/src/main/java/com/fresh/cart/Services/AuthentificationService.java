package com.fresh.cart.Services;

import com.fresh.cart.Entities.Authentification;
import com.fresh.cart.Entities.Utilisateur;
import com.fresh.cart.Repositories.AuthentificationRepository;
import com.fresh.cart.Repositories.UtilisateurRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthentificationService {
    private final UtilisateurRepository utilisateurRepository;
    private final AuthentificationRepository authentificationRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JavaMailSender mailSender;

    public AuthentificationService(UtilisateurRepository utilisateurRepository, AuthentificationRepository authentificationRepository, JavaMailSender mailSender) {
        this.utilisateurRepository = utilisateurRepository;
        this.authentificationRepository = authentificationRepository;
        this.mailSender = mailSender;
    }

    public Utilisateur inscrireUtilisateur(Utilisateur utilisateur, String motDePasse) throws MessagingException {

        Authentification auth = new Authentification();
        String token = UUID.randomUUID().toString();

        // Hashage du mot de passe
        String motDePasseHashe = passwordEncoder.encode(motDePasse);

        // Enregistrement de l'utilisateur
        Utilisateur nouvelUtilisateur = utilisateurRepository.save(utilisateur);

        // Enregistrement des informations d'authentification
        auth.setUtilisateur(utilisateur);
        auth.setMotDePasse(motDePasseHashe);
        auth.setEmailVerifie(false);
        auth.setToken(token);

        authentificationRepository.save(auth);

        // Appel de la méthode d'envoi de l'e-mail de vérification
        String verificationLink = "http://192.168.0.180:8080/auth/verify?token=" + token;
        envoyerEmailVerification(utilisateur.getCourriel(), verificationLink);

        return nouvelUtilisateur;
    }

    private void envoyerEmailVerification(String email, String verificationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Vérifiez votre compte FreshCart");
        helper.setText("<h2>Bienvenue sur FreshCart!</h2>"
                + "<p>Cliquez sur le lien suivant pour activer votre compte :</p>"
                + "<a href='" + verificationLink + "'>Activer mon compte</a>", true);

        mailSender.send(message);
    }

    public boolean verifierEmail(String token) {
        Authentification auth = authentificationRepository.findByToken(token);
        if (auth != null && !auth.isEmailVerifie()) { // Vérifiez si déjà vérifié
            auth.setEmailVerifie(true);
            authentificationRepository.save(auth); // Sauvegardez la modification
            return true;
        }
        return false;
    }

    public Utilisateur connecterUtilisateur(String courriel, String motDePasse) {
        Authentification auth = authentificationRepository.findByUtilisateurCourriel(courriel);

        if (auth == null) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }

        if (!auth.isEmailVerifie()) {
            throw new RuntimeException("Veuillez vérifier votre adresse courriel avant de vous connecter.");
        }

        if (!passwordEncoder.matches(motDePasse, auth.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect.");
        }

        return auth.getUtilisateur();
    }

    public void motDePasseOublie(String courriel) {
        // Vérifier si l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findByCourriel(courriel);

        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }

        //Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier si l'utilisateur a une entrée d'authentification
        Authentification auth = authentificationRepository.findByUtilisateurCourriel(courriel);

        if (auth == null) {
            throw new RuntimeException("Aucune authentification trouvée pour cet utilisateur.");
        }

        // Générer un token unique pour la réinitialisation du mot de passe
        String token = UUID.randomUUID().toString();
        auth.setToken(token);

        // Sauvegarder le token en base de données
        authentificationRepository.save(auth);

        // Construire le lien de réinitialisation
        String resetLink = "http://192.168.0.180:8080/auth/reset-password?token=" + token;

        // Envoyer l'e-mail de réinitialisation
        envoyerEmailResetPassword(courriel, resetLink);
    }


//    public void motDePasseOublie(String courriel) {
//        Authentification auth = authentificationRepository.findByUtilisateurCourriel(courriel);
//
//        if (auth == null) {
//            throw new RuntimeException("Utilisateur non trouvé.");
//        }
//
//        String token = UUID.randomUUID().toString();
//        auth.setToken(token);
//        authentificationRepository.save(auth);
//
//        String resetLink = "http://192.168.0.180:8080/auth/reset-password?token=" + token;
//        envoyerEmailResetPassword(courriel, resetLink);
//    }

    private void envoyerEmailResetPassword(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Réinitialisation de votre mot de passe");
        message.setText("Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink);
        mailSender.send(message);
    }

    public void reinitialiserMotDePasse(String token, String nouveauMotDePasse) {
        Authentification auth = authentificationRepository.findByToken(token);

        if (auth == null) {
            throw new RuntimeException("Token invalide ou expiré.");
        }

        auth.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        auth.setToken(null);
        authentificationRepository.save(auth);
    }

    public void deconnecterUtilisateur(String courriel) {
        Authentification auth = authentificationRepository.findByUtilisateurCourriel(courriel);
        if (auth != null) {
            auth.setToken(null); // Invalider le token de session
            authentificationRepository.save(auth);
        }
    }
}