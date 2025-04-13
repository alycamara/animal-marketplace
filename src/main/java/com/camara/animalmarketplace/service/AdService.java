package com.camara.animalmarketplace.service;

import com.camara.animalmarketplace.exception.ResourceNotFoundException;
import com.camara.animalmarketplace.model.Ad;
import com.camara.animalmarketplace.model.Animal;
import com.camara.animalmarketplace.model.Role;
import com.camara.animalmarketplace.model.User;
import com.camara.animalmarketplace.repository.AdRepository;
import com.camara.animalmarketplace.repository.AnimalRepository;
import com.camara.animalmarketplace.repository.UserRepository;
import com.camara.animalmarketplace.specification.AdSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {
    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

   /* public List<Ad> getAllAds() {
        return adRepository.findAll();
    }*/

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


    public Optional<Ad> getAdById(Long id) {
        return adRepository.findById(id);
    }

    public Ad createAd(Ad ad) {
        if (ad.getSeller() != null && ad.getSeller().getId() == null) {
            ad.getSeller().setRole(Role.SELLER);
            ad.setSeller(userRepository.save(ad.getSeller()));
        }
        if (ad.getAnimal() != null && ad.getAnimal().getId() == null) {
            ad.setAnimal(animalRepository.save(ad.getAnimal()));
        }
        return adRepository.save(ad);
    }

   /* public void updateAd(Long id, Ad adDetails) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        ad.setTitle(adDetails.getTitle());
        ad.setPrice(adDetails.getPrice());
        ad.setLocation(adDetails.getLocation());
        ad.setAnimal(adDetails.getAnimal());
        ad.setSeller(adDetails.getSeller());
        adRepository.save(ad);
    }*/

    public void updateAd(Long id, Ad adDetails) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        // Mise à jour des champs de l'annonce
        if (adDetails.getTitle() != null)  {
            ad.setTitle(adDetails.getTitle());
        }

        ad.setPrice(adDetails.getPrice());

        if (adDetails.getLocation() != null)
            ad.setLocation(adDetails.getLocation());

        // Mise à jour de l'animal
        if (adDetails.getAnimal() != null) {
            if (adDetails.getAnimal().getId() != null) {
                Animal existingAnimal = animalRepository.findById(adDetails.getAnimal().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Animal not found"));
                if (adDetails.getAnimal().getSpecies() != null)
                    existingAnimal.setSpecies(adDetails.getAnimal().getSpecies());
                if (adDetails.getAnimal().getBreed() != null)
                    existingAnimal.setBreed(adDetails.getAnimal().getBreed());

                existingAnimal.setAge(adDetails.getAnimal().getAge());
                if (adDetails.getAnimal().getSex() != null)
                    existingAnimal.setSex(adDetails.getAnimal().getSex());
                ad.setAnimal(existingAnimal);
            } else {
                ad.setAnimal(animalRepository.save(adDetails.getAnimal()));
            }
        }

        // Mise à jour du vendeur
        if (adDetails.getSeller() != null) {
            if (adDetails.getSeller().getId() != null) {
                User existingUser = userRepository.findById(adDetails.getSeller().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
                if (adDetails.getSeller().getEmail() != null)
                    existingUser.setEmail(adDetails.getSeller().getEmail());
                if (adDetails.getSeller().getName() != null)
                    existingUser.setName(adDetails.getSeller().getName());
                if (adDetails.getSeller().getRole() != null)
                    existingUser.setRole(adDetails.getSeller().getRole());
                ad.setSeller(existingUser);
            } else {
                ad.setSeller(userRepository.save(adDetails.getSeller()));
            }
        }

        // Sauvegarde de l'annonce
        adRepository.save(ad);
    }

    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        adRepository.delete(ad);
    }
}
