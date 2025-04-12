package om.camara.animalmarketplace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "animals")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    @Column(name = "image_url")
    private String imageUrl;

    // Constructeurs
    public Animal() {
    }

    public Animal(String titre, String description, String imageUrl) {
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
