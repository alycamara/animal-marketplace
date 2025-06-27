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
        MockitoAnnotations.openMocks(this);
        files = new MultipartFile[] { mock(MultipartFile.class), mock(MultipartFile.class) }; // Initialisation explicite
    }

    @Test
    void testGetAllAds() {
        List<Ad> ads = new ArrayList<>();
        when(adService.findAds(null, null, null)).thenReturn(ads);

        String viewName = adController.getAllAds(null, null, null, model);

        verify(model).addAttribute("ads", ads);
        assertEquals("public/list", viewName);
    }

    @Test
    void testShowCreateForm() {

        when(userService.getAuthenticatedUser()).thenReturn(new User());
        String viewName = adController.showCreateForm(model);

        verify(model).addAttribute(eq("ad"), any(Ad.class));
        assertEquals("private/create", viewName);
    }

    @Test
    void testGetAdById() {
        Ad ad = new Ad();
        when(adService.getAdById(1L)).thenReturn(Optional.of(ad));

        String viewName = adController.getAdById(1L, model);

        verify(model).addAttribute("ad", ad);
        assertEquals("public/detail", viewName);
    }

    @Test
    void testHandleCreateAd_WithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adController.handleCreateAd(new Ad(), bindingResult, redirectAttributes, files);

        assertEquals("private/create", viewName);
    }

    @Test
    void testHandleCreateAd_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adController.handleCreateAd(new Ad(), bindingResult, redirectAttributes, files);

        verify(adService).createAd(any(Ad.class), eq(files));
        verify(redirectAttributes).addFlashAttribute("successMessage", "L'annonce a été créée avec succès !");
        assertEquals("redirect:/ads", viewName);
    }

    @Test
    void testHandleDeleteAd() {
        String viewName = adController.handleDeleteAd(1L, new Ad(), redirectAttributes);

        verify(adService).deleteAd(1L);
        verify(redirectAttributes).addFlashAttribute("successMessage", "L'annonce a été supprimée avec succès !");
        assertEquals("redirect:/ads", viewName);
    }

    @Test
    void testFindAdByIdOrThrow_ThrowsException() {
        when(adService.getAdById(1L)).thenReturn(Optional.empty());

        try {
            adController.getAdById(1L, model);
        } catch (ResourceNotFoundException e) {
            assertEquals("Ad not found", e.getMessage());
        }
    }
}