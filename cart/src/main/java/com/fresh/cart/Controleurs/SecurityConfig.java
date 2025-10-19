package com.fresh.cart.Controleurs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
/* on autorise l.'accés au /auth/** pour tous les gens meme s'ils sont pas connectes
}*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Désactiver CSRF pour simplifier les tests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Autoriser l'inscription et la vérification
                        .anyRequest().authenticated()  // Authentification pour les autres requêtes
                );

        return http.build();
    }
}