package com.camara.animalmarketplace.service;

import com.camara.animalmarketplace.exception.ResourceNotFoundException;
import com.camara.animalmarketplace.model.*;
import com.camara.animalmarketplace.repository.AdRepository;
import com.camara.animalmarketplace.repository.AnimalRepository;
import com.camara.animalmarketplace.repository.PhotoRepository;
import com.camara.animalmarketplace.repository.UserRepository;
import com.camara.animalmarketplace.specification.AdSpecifications;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les annonces
 */
@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final S3Service s3Service;
    private final PhotoRepository photoRepository;

    public AdService(AdRepository adRepository, UserRepository userRepository, AnimalRepository animalRepository, S3Service s3Service, PhotoRepository photoRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.s3Service = s3Service;
        this.photoRepository = photoRepository;
    }

    /**
     * Récupère toutes les annonces
     *
     * @param species
     * @param location
     * @param sort
     * @return
     */
    public List<Ad> findAds(String species, String location, String sort) {
        Specification<Ad> spec = Specification.where(null);

        if (species != null && !species.isEmpty()) {
            spec = spec.and(AdSpecifications.hasSpecies(species));
        }
        if (location != null && !location.isEmpty()) {
            spec = spec.and(AdSpecifications.hasLocation(location));
        }

        Sort sortOrder = Sort.by(Sort.Direction.DESC, "createdAt"); // Par défaut, trier par date de création
        if (sort != null) {
            switch (sort) {
                case "price":
                    sortOrder = Sort.by(Sort.Direction.ASC, "price");
                    break;
                case "location":
                    sortOrder = Sort.by(Sort.Direction.ASC, "location");
                    break;
            }
        }

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sortOrder); // Utiliser Pageable pour le tri

        return adRepository.findAll(spec, pageable).getContent();
    }

    /**
     * Récupère une annonce par son ID
     *
     * @param id
     * @return
     */

    public Optional<Ad> getAdById(Long id) {
        return adRepository.findById(id);
    }

    /**
     * Crée une annonce
     *
     * @param ad
     * @param files
     * @return
     */
    public Ad createAd(Ad ad, MultipartFile[] files) {
        if (ad.getSeller() != null && ad.getSeller().getId() == null) {
            ad.setSeller(userRepository.save(ad.getSeller()));
        }
        if (ad.getAnimal() != null && ad.getAnimal().getId() == null) {
            ad.setAnimal(animalRepository.save(ad.getAnimal()));
        }

        // Enregistrement des photos
        savePhotos(files, ad);

        return adRepository.save(ad);
    }

    /**
     * Met à jour une annonce
     *
     * @param id
     * @param adDetails
     * @param files
     * @param deletePhotoIds
     */
    public void updateAd(Long id, Ad adDetails, MultipartFile[] files, List<Long> deletePhotoIds) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        // Mise à jour des champs de l'annonce
        if (adDetails.getTitle() != null) {
            ad.setTitle(adDetails.getTitle());
        }

        ad.setPrice(adDetails.getPrice());

        if (adDetails.getLocation() != null)
            ad.setLocation(adDetails.getLocation());

        // Mise à jour de l'animal
        ad.setAnimal(updateOrCreateAnimal(adDetails.getAnimal()));

        // Mise à jour du vendeur
        ad.setSeller(updateOrCreateSeller(adDetails.getSeller()));

        // Suppression des photos sélectionnées
        deleteIdsPhotos(deletePhotoIds, ad);

        // enregistrement des nouveaux photos
        savePhotos(files, ad);

        // Sauvegarde de l'annonce
        adRepository.save(ad);
    }

    /**
     * Supprime une annonce
     *
     * @param id
     */

    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        List<Photo> photos = ad.getPhotos();

        // Suppression des photos de l'annonce dans S3
        if (photos != null && !photos.isEmpty()) {
            photos.forEach(photo -> s3Service.deleteFile(photo.getFileName()));
        }

        // Suppression de l'annonce et des photos associées
        photoRepository.deleteAll(photos);
        adRepository.delete(ad);
    }

    /**
     * Met à jour ou crée un vendeur
     *
     * @param sellerDetails
     * @return
     */
    public User updateOrCreateSeller(User sellerDetails) {
        if (sellerDetails.getId() != null) {
            User existingUser = userRepository.findById(sellerDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            if (sellerDetails.getEmail() != null)
                existingUser.setEmail(sellerDetails.getEmail());
            if (sellerDetails.getName() != null)
                existingUser.setName(sellerDetails.getName());
            if (sellerDetails.getPhone() != null)
                existingUser.setPhone(sellerDetails.getPhone());
            return existingUser;
        } else {
            return userRepository.save(sellerDetails);
        }
    }

    /**
     * Met à jour ou crée un animal
     *
     * @param animalDetails
     * @return
     */
    private Animal updateOrCreateAnimal(Animal animalDetails) {
        if (animalDetails.getId() != null) {
            Animal existingAnimal = animalRepository.findById(animalDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Animal not found"));
            if (animalDetails.getSpecies() != null)
                existingAnimal.setSpecies(animalDetails.getSpecies());
            if (animalDetails.getBreed() != null)
                existingAnimal.setBreed(animalDetails.getBreed());

            existingAnimal.setAge(animalDetails.getAge());
            if (animalDetails.getSex() != null)
                existingAnimal.setSex(animalDetails.getSex());
            return existingAnimal;
        } else {
            return animalRepository.save(animalDetails);
        }
    }

    /**
     * Sauvegarde les photos de l'annonce
     *
     * @param files
     * @param ad
     */
    private void savePhotos(MultipartFile[] files, Ad ad) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String url = s3Service.uploadFile(file);
                Photo photo = new Photo();
                photo.setUrl(url);
                photo.setFileName(fileName);
                photo.setAd(ad);
                ad.getPhotos().add(photo);
            }
        }
    }

    /**
     * Supprime les photos de l'annonce
     *
     * @param deletePhotoIds
     * @param ad
     */
    private void deleteIdsPhotos(List<Long> deletePhotoIds, Ad ad) {
        if (deletePhotoIds != null && !deletePhotoIds.isEmpty()) {
            deletePhotoIds.stream()
                    .map(id -> photoRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Photo not found with ID: " + id)))
                    .forEach(photo -> {
                        photoRepository.delete(photo);
                        s3Service.deleteFile(photo.getFileName());
                        ad.getPhotos().remove(photo); // Retirer la photo de l'annonce
                    });
        }
    }
}
