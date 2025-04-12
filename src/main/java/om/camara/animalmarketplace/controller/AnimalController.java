package om.camara.animalmarketplace.controller;

import om.camara.animalmarketplace.model.Animal;
import om.camara.animalmarketplace.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/annonces")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public String listAnnonces(Model model) {
        model.addAttribute("annonces", animalService.findAll());
       for (Animal animal : animalService.findAll()) {
           System.out.println( "animal : " +animal.getImageUrl());

       }
        return "annonces";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("animal", new Animal());
        return "createAnnonce";
    }

    @PostMapping
    public String createAnnonce(Animal animal) {
        animalService.save(animal);
        return "redirect:/annonces";
    }
}
