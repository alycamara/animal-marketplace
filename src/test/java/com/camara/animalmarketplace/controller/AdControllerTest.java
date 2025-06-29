package com.camara.animalmarketplace.controller;

import com.camara.animalmarketplace.exception.ResourceNotFoundException;
import com.camara.animalmarketplace.model.Ad;
import com.camara.animalmarketplace.model.User;
import com.camara.animalmarketplace.service.AdService;
import com.camara.animalmarketplace.service.S3Service;
import com.camara.animalmarketplace.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdControllerTest {

    @Mock
    private AdService adService;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AdController adController;

    private MultipartFile[] files;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks avant chaque test
        MockitoAnnotations.openMocks(this);
        files = new MultipartFile[] { mock(MultipartFile.class), mock(MultipartFile.class) }; // Initialisation explicite
    }

    @Test
    void testGetAllAds() {
        // Test de la méthode pour récupérer toutes les annonces
        List<Ad> ads = new ArrayList<>();
        when(adService.findAds(null, null, null)).thenReturn(ads);

        String viewName = adController.getAllAds(null, null, null, model);

        // Vérifie que les annonces sont ajoutées au modèle
        verify(model).addAttribute("ads", ads);
        assertEquals("public/list", viewName);
    }

    @Test
    void testShowCreateForm() {
        // Test de la méthode pour afficher le formulaire de création
        when(userService.getAuthenticatedUser()).thenReturn(new User());
        String viewName = adController.showCreateForm(model);

        // Vérifie que le modèle contient une nouvelle annonce
        verify(model).addAttribute(eq("ad"), any(Ad.class));
        assertEquals("private/create", viewName);
    }

    @Test
    void testGetAdById() {
        // Test de la méthode pour récupérer une annonce par son ID
        Ad ad = new Ad();
        when(adService.getAdById(1L)).thenReturn(Optional.of(ad));

        String viewName = adController.getAdById(1L, model);

        // Vérifie que l'annonce est ajoutée au modèle
        verify(model).addAttribute("ad", ad);
        assertEquals("public/detail", viewName);
    }

    @Test
    void testHandleCreateAd_WithErrors() {
        // Test de la méthode de création avec des erreurs de validation
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adController.handleCreateAd(new Ad(), bindingResult, redirectAttributes, files);

        // Vérifie que la vue reste sur le formulaire de création
        assertEquals("private/create", viewName);
    }

    @Test
    void testHandleCreateAd_Success() {
        // Test de la méthode de création sans erreurs
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adController.handleCreateAd(new Ad(), bindingResult, redirectAttributes, files);

        // Vérifie que l'annonce est créée et un message de succès est ajouté
        verify(adService).createAd(any(Ad.class), eq(files));
        verify(redirectAttributes).addFlashAttribute("successMessage", "L'annonce a été créée avec succès !");
        assertEquals("redirect:/ads", viewName);
    }

    @Test
    void testHandleDeleteAd() {
        // Test de la méthode pour supprimer une annonce
        String viewName = adController.handleDeleteAd(1L, new Ad(), redirectAttributes);

        // Vérifie que l'annonce est supprimée et un message de succès est ajouté
        verify(adService).deleteAd(1L);
        verify(redirectAttributes).addFlashAttribute("successMessage", "L'annonce a été supprimée avec succès !");
        assertEquals("redirect:/ads", viewName);
    }

    @Test
    void testFindAdByIdOrThrow_ThrowsException() {
        // Test de la méthode pour lever une exception si l'annonce n'est pas trouvée
        when(adService.getAdById(1L)).thenReturn(Optional.empty());

        try {
            adController.getAdById(1L, model);
        } catch (ResourceNotFoundException e) {
            // Vérifie que l'exception contient le bon message
            assertEquals("Ad not found", e.getMessage());
        }
    }

    @Test
    void testShowEditForm() {
        // Test de la méthode pour afficher le formulaire d'édition
        Ad ad = new Ad();
        when(adService.getAdById(1L)).thenReturn(Optional.of(ad));

        String viewName = adController.showEditForm(1L, model);

        // Vérifie que l'annonce est ajoutée au modèle
        verify(model).addAttribute("ad", ad);
        assertEquals("private/edit", viewName);
    }

    @Test
    void testHandleUpdateAd_WithErrors() {
        // Test de la méthode de mise à jour avec des erreurs de validation
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adController.handleUpdateAd(1L, new Ad(), redirectAttributes, files, bindingResult, null);

        // Vérifie que la vue reste sur le formulaire de mise à jour
        assertEquals("/update/{id}", viewName);
    }

    @Test
    void testHandleUpdateAd_Success() {
        // Test de la méthode de mise à jour sans erreurs
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adController.handleUpdateAd(1L, new Ad(), redirectAttributes, files, bindingResult, null);

        // Vérifie que l'annonce est mise à jour et un message de succès est ajouté
        verify(adService).updateAd(eq(1L), any(Ad.class), eq(files), eq(null));
        verify(redirectAttributes).addFlashAttribute("successMessage", "L'annonce a été mis à jour avec succès !");
        assertEquals("redirect:/ads", viewName);
    }

    @Test
    void testShowDeleteForm() {
        // Test de la méthode pour afficher le formulaire de suppression
        Ad ad = new Ad();
        when(adService.getAdById(1L)).thenReturn(Optional.of(ad));

        String viewName = adController.showDeleteForm(1L, model);

        // Vérifie que l'annonce est ajoutée au modèle
        verify(model).addAttribute("ad", ad);
        assertEquals("private/delete", viewName);
    }
}