package om.camara.animalmarketplace.controller;

import om.camara.animalmarketplace.exception.ResourceNotFoundException;
import om.camara.animalmarketplace.model.Ad;
import om.camara.animalmarketplace.model.Photo;
import om.camara.animalmarketplace.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ads")
public class AdController {
    @Autowired
    private AdService adService;

   @GetMapping()
    public String getAllAds(@RequestParam(required = false) String species,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String sort,
                            Model model) {
        List<Ad> ads = adService.findAds(species, location, sort);
        model.addAttribute("ads", ads);
        return "ad/list";
    }

    @GetMapping("/{id}")
    public String getAdById(@PathVariable Long id, Model model) {
        Ad ad = adService.getAdById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));

        if (ad.getPhotos() == null || ad.getPhotos().isEmpty()) {
            System.out.println("---------------Aucune photo disponible pour cette annonce.---------------");
        }
        for (Photo photo :ad.getPhotos()){
            System.out.println("---------------Photo URL: " + photo.getUrl() + "---------------");
        }
        model.addAttribute("ad", ad);
        return "ad/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ad", new Ad());
        return "ad/create";
    }

    @PostMapping
    public String createAd(@ModelAttribute Ad ad) {
        adService.createAd(ad);
        return "redirect:/ads";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Ad ad = adService.getAdById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        model.addAttribute("ad", ad);
        return "ad/edit";
    }

    @PostMapping("/update/{id}")
    public String updateAd(@PathVariable Long id, @ModelAttribute Ad adDetails) {
        adService.updateAd(id, adDetails);
        return "redirect:/ads";
    }

    @GetMapping("/delete/{id}")
    public String deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return "redirect:/ads";
    }
}
