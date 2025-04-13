package om.camara.animalmarketplace.service;

import om.camara.animalmarketplace.exception.ResourceNotFoundException;
import om.camara.animalmarketplace.model.Ad;
import om.camara.animalmarketplace.repository.AdRepository;
import om.camara.animalmarketplace.specification.AdSpecifications;
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

        Sort sortOrder = Sort.by(Sort.Direction.ASC, "createdAt"); // Par défaut, trier par date de création
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
        return adRepository.save(ad);
    }

    public Ad updateAd(Long id, Ad adDetails) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        //ad.getsetTitle(adDetails.getTitle());
        //ad.setPrice(adDetails.getPrice());
        //ad.setLocation(adDetails.getLocation());
        //ad.setAnimal(adDetails.getAnimal());
        //ad.setSeller(adDetails.getSeller());
        return adRepository.save(ad);
    }

    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        adRepository.delete(ad);
    }
}
