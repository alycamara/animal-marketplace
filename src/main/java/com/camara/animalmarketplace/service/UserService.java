package com.camara.animalmarketplace.service;

import com.camara.animalmarketplace.controller.AdController;
import com.camara.animalmarketplace.model.User;
import com.camara.animalmarketplace.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();


            if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;

                String phone = oidcUser.getPhoneNumber() ;
                String name = oidcUser.getName();// Assurez-vous que `credentials` contient le téléphone
                String email = oidcUser.getEmail(); // Assurez-vous que `credentials` contient l'email

                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setPhone(phone);
                return user;
            }

        }
        return null;
    }
}

