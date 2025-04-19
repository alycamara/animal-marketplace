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
    private UserRepository userRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private AdService adService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAds() {
        Ad ad = new Ad();
        ad.setId(1L);
        Page<Ad> page = new PageImpl<>(List.of(ad));
        when(adRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        List<Ad> ads = adService.findAds("dog", "Paris", "price");

        assertNotNull(ads);
        assertEquals(1, ads.size());
        verify(adRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testGetAdById() {
        Ad ad = new Ad();
        ad.setId(1L);
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        Optional<Ad> result = adService.getAdById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(adRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateAd() {
        Ad ad = new Ad();
        ad.setId(1L);
        when(adRepository.save(any(Ad.class))).thenReturn(ad);

        Ad result = adService.createAd(ad, new MultipartFile[0]);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(adRepository, times(1)).save(any(Ad.class));
    }

    @Test
    void testUpdateAd() {
        Ad ad = new Ad();
        ad.setId(1L);
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        Ad updatedAd = new Ad();
        updatedAd.setTitle("Updated Title");
        updatedAd.setPrice(100.0);

        adService.updateAd(1L, updatedAd, new MultipartFile[0], null);

        assertEquals("Updated Title", ad.getTitle());
        assertEquals(100.0, ad.getPrice());
        verify(adRepository, times(1)).save(ad);
    }

    @Test
    void testDeleteAd() {
        Ad ad = new Ad();
        ad.setId(1L);
        Photo photo = new Photo();
        photo.setFileName("test.jpg");
        ad.setPhotos(List.of(photo));
        when(adRepository.findById(1L)).thenReturn(Optional.of(ad));

        adService.deleteAd(1L);

        verify(s3Service, times(1)).deleteFile("test.jpg");
        verify(photoRepository, times(1)).deleteAll(ad.getPhotos());
        verify(adRepository, times(1)).delete(ad);
    }

    @Test
    void testUpdateOrCreateSeller() {
        User seller = new User();
        seller.setId(1L);
        seller.setEmail("test@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));

        User updatedSeller = new User();
        updatedSeller.setId(1L);
        updatedSeller.setEmail("updated@example.com");

        User result = adService.updateOrCreateSeller(updatedSeller);

        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }
}