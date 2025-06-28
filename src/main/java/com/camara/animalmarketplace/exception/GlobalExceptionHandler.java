package com.camara.animalmarketplace.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Gestionnaire global des exceptions
 * Cette classe gère les exceptions qui peuvent survenir dans l'application.
 * Elle redirige l'utilisateur vers la page d'accueil avec un message d'erreur approprié.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gestion des exceptions de ressource introuvable
     *
     * @param ex
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, RedirectAttributes redirectAttributes) {
        String errorId = java.util.UUID.randomUUID().toString(); // Génère un identifiant unique
        logger.error("Ressource introuvable (ID: {}): {}", errorId, ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", "La ressource demandée est introuvable. (ID: " + errorId + ")");
        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    /**
     * Gestion des exceptions de validation
     *
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {

        // Gestion des autres exceptions générales
        String errorId = java.util.UUID.randomUUID().toString();
        logger.error("Une erreur inattendue est survenue (ID: {}): {}", errorId, ex.getMessage(), ex);
        model.addAttribute("errorMessage", "Une erreur inattendue est survenue. Veuillez réessayer plus tard. (ID: " + errorId + ")");
        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    /**
     * Gestion des exceptions S3Exception
     *
     * @param ex
     * @param model
     * @return
     */
    @ExceptionHandler(S3Exception.class)
    public String handleS3Exception(S3Exception ex, Model model) {
        String errorId = java.util.UUID.randomUUID().toString(); // Génère un identifiant unique
        logger.error("Erreur S3 AWS (ID: {}): {}", errorId, ex.getMessage(), ex);
        model.addAttribute("errorMessage", "Une erreur est survenue avec le service S3 AWS. Veuillez réessayer plus tard. (ID: " + errorId + ")");
        return "redirect:/ads"; // Redirection vers la liste des annonces
    }



}