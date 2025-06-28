package com.camara.animalmarketplace.service;


import com.camara.animalmarketplace.model.Animal;
import com.camara.animalmarketplace.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    public Animal findById(Long id) {
        return animalRepository.findById(id).orElse(null);
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    public void deleteById(Long id) {
        animalRepository.deleteById(id);
    }


}

