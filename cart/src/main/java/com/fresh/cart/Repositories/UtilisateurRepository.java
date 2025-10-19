package com.fresh.cart.Repositories;

import com.fresh.cart.Entities.Utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Utilisateur findByCourriel(String courriel);
}
