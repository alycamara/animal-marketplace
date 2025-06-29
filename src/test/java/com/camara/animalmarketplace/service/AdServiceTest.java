package com.camara.animalmarketplace.service;

import com.camara.animalmarketplace.model.*;
import com.camara.animalmarketplace.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdService adService;

    @BeforeEach
    void setUp() {
        // Initialisation des mocks avant chaque test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAds() {
        // Test de la méthode findAds pour récupérer les annonces avec des critères spécifiques
        Ad ad = new Ad();
        ad.setId(1L);
        Page<Ad> page = new PageImpl<>(List.of(ad));
        when(adRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Appel de la méthode avec des critères
        List<Ad> ads = adService.findAds("dog", "Paris", "price");

        // Vérifications
        assertNotNull(ads); // Vérifie que la liste des annonces n'est pas nulle
        assertEquals(1, ads.size()); // Vérifie que la liste contient une annonce
        verify(adRepository, times(1)).findAll(any(Specification.class), any(Pageable.class)); // Vérifie que la méthode findAll a été appelée une fois
    }

    @Test
    void testGetAdById() {
        // Test de la méthode getAdById pour récupérer une annonce par son ID
        Ad ad = new Ad();
        ad.setId(1L);
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        // Appel de la méthode
        Optional<Ad> result = adService.getAdById(1L);

        // Vérifications
        assertTrue(result.isPresent()); // Vérifie que l'annonce est présente
        assertEquals(1L, result.get().getId()); // Vérifie que l'ID de l'annonce correspond
        verify(adRepository, times(1)).findById(1L); // Vérifie que la méthode findById a été appelée une fois
    }

    @Test
    void testCreateAd() {
        // Test de la méthode createAd pour créer une nouvelle annonce
        Ad ad = new Ad();
        ad.setId(1L);
        when(adRepository.save(any(Ad.class))).thenReturn(ad);
        when(userService.getAuthenticatedUser()).thenReturn(new User()); // Simule l'utilisateur authentifié

        // Appel de la méthode
        Ad result = adService.createAd(ad, new MultipartFile[0]);

        // Vérifications
        assertNotNull(result); // Vérifie que l'annonce créée n'est pas nulle
        assertEquals(1L, result.getId()); // Vérifie que l'ID de l'annonce correspond
        verify(adRepository, times(1)).save(any(Ad.class)); // Vérifie que la méthode save a été appelée une fois
    }

    @Test
    void testUpdateAd() {
        // Test de la méthode updateAd pour mettre à jour une annonce existante
        Ad ad = new Ad();
        User seller = new User();
        seller.setEmail("test.com");
        ad.setSeller(seller);
        ad.setId(1L);
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        // Création d'une annonce mise à jour
        Ad updatedAd = new Ad();
        updatedAd.setTitle("Updated Title");
        updatedAd.setPrice(100.0);
        User sellerUpdated = new User();
        sellerUpdated.setEmail("test.com");
        updatedAd.setSeller(sellerUpdated);

        // Vérification que les détails du vendeur ne sont pas nuls
        assertNotNull(updatedAd.getSeller(), "Les détails du vendeur ne doivent pas être null");
        assertNotNull(updatedAd.getSeller().getEmail(), "L'email du vendeur ne doit pas être null");

        when(adRepository.save(any(Ad.class))).thenReturn(ad); // Simule la sauvegarde de l'annonce mise à jour
        when(userService.findByEmail("test.com")).thenReturn(seller); // Simule la récupération du vendeur par email

        // Appel de la méthode
        adService.updateAd(1L, updatedAd, new MultipartFile[0], null);

        // Vérifications
        assertEquals("Updated Title", ad.getTitle()); // Vérifie que le titre a été mis à jour
        assertEquals(100.0, ad.getPrice()); // Vérifie que le prix a été mis à jour
        verify(adRepository, times(1)).save(ad); // Vérifie que la méthode save a été appelée une fois
    }

    @Test
    void testDeleteAd() {
        // Test de la méthode deleteAd pour supprimer une annonce
        Ad ad = new Ad();
        ad.setId(1L);
        Photo photo = new Photo();
        photo.setFileName("test.jpg");
        ad.setPhotos(List.of(photo));
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        // Appel de la méthode
        adService.deleteAd(1L);

        // Vérifications
        verify(s3Service, times(1)).deleteFile("test.jpg"); // Vérifie que le fichier a été supprimé de S3
        verify(photoRepository, times(1)).deleteAll(ad.getPhotos()); // Vérifie que les photos ont été supprimées
        verify(adRepository, times(1)).delete(ad); // Vérifie que l'annonce a été supprimée
    }

    @Test
    void testUpdateOrCreateSeller() {
        // Test de la méthode updateOrCreateSeller pour mettre à jour ou créer un vendeur
        User seller = new User();
        seller.setId(1L);
        seller.setPhone("0712345678");
        seller.setName("Christian");
        seller.setEmail("christian@test.com");
        when(userService.findByEmail("christian@test.com")).thenReturn(seller);

        // Vérification que l'email du vendeur n'est pas null
        assertNotNull(seller.getEmail(), "L'email du vendeur ne doit pas être null");

        // Création d'un vendeur mis à jour
        User updatedSeller = new User();
        updatedSeller.setId(1L);
        updatedSeller.setPhone("0712345678");
        updatedSeller.setName("Christian Updated");
        updatedSeller.setEmail("christian@test.com");
        when(userService.save(seller)).thenReturn(updatedSeller);

        // Appel de la méthode
        User result = adService.updateOrCreateSeller(updatedSeller);

        // Vérifications
        assertNotNull(result); // Vérifie que le résultat n'est pas nul
        assertEquals("Christian Updated", result.getName()); // Vérifie que le nom du vendeur a été mis à jour
    }
}