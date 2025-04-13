package om.camara.animalmarketplace.specification;

import org.springframework.data.jpa.domain.Specification;
import om.camara.animalmarketplace.model.*;

public class AdSpecifications {
    public static Specification<Ad> hasSpecies(String species) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("animal").get("species"), species);
    }

    public static Specification<Ad> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("location"), location);
    }
}
