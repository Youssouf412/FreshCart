package com.fresh.cart.Repositories;

import com.fresh.cart.Entities.Authentification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthentificationRepository extends JpaRepository<Authentification, Integer> {
    Authentification findByUtilisateurCourriel(String courriel);
    Authentification findByToken(String token);
}