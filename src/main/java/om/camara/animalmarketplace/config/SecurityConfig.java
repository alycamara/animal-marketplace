package om.camara.animalmarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/ads/**").permitAll() // Autoriser l'accès aux pages des annonces
                .anyRequest().authenticated() // Authentification pour les autres pages
            )
            .csrf(csrf -> csrf.disable()); // Désactiver CSRF avec la nouvelle API

        return http.build();
    }
}