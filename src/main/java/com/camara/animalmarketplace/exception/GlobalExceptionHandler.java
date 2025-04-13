package com.camara.animalmarketplace.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Gestion des exceptions ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        logger.error("Resource not found: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "La ressource demandée est introuvable.");
        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    // Gestion des exceptions générales
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("An error occurred: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Une erreur inattendue est survenue. Veuillez réessayer plus tard.");
        return "redirect:/ads"; // Redirection vers la liste des annonces
    }
}