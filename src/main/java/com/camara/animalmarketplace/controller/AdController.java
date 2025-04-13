package com.camara.animalmarketplace.controller;

import com.camara.animalmarketplace.exception.ResourceNotFoundException;
import com.camara.animalmarketplace.model.Ad;
import com.camara.animalmarketplace.service.AdService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ads")
public class AdController {

    private static final Logger logger = LoggerFactory.getLogger(AdController.class);

    private  final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

   @GetMapping()
    public String getAllAds(@RequestParam(required = false) String species,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String sort,
                            Model model) {
        List<Ad> ads = adService.findAds(species, location, sort);
        model.addAttribute("ads", ads);
        return "ad/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("ad", new Ad());
        return "ad/create";
    }

    @GetMapping("/{id}")
    public String getAdById(@PathVariable Long id, Model model) {
        Ad ad = adService.getAdById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        model.addAttribute("ad", ad);
        return "ad/detail";
    }


    @PostMapping
    public String handleCreateAd(@Valid @ModelAttribute Ad ad, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "ad/create"; // Retourne le formulaire avec les erreurs
        }
        try {
            adService.createAd(ad); // Sauvegarde de l'annonce
            redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été créée avec succès !");
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la création de l'annonce", e);
            throw e;
        }

        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Ad ad = findAdByIdOrThrow (id);
        model.addAttribute("ad", ad);
        return "ad/edit";
    }

    @PostMapping("/update/{id}")
    public String handleUpdateAd(@PathVariable Long id, @ModelAttribute Ad adDetails, RedirectAttributes redirectAttributes) {

        try {
            adService.updateAd(id, adDetails);; // Sauvegarde de l'annonce
            redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été mis à jour avec succès !");
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la mise à jour de l'annonce", e);
            throw e;
        }

        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Ad ad = findAdByIdOrThrow (id);
        model.addAttribute("ad", ad);
        return "ad/delete";
    }

    @PostMapping("/delete/{id}")
    public String handleDeleteAd(@PathVariable Long id,@ModelAttribute Ad adDetails, RedirectAttributes redirectAttributes) {
        try{
            adService.deleteAd(id);
            redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été supprimée avec succès !");
        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la supression de l'annonce", e);
        }

        return "redirect:/ads";
    }

    private Ad findAdByIdOrThrow(Long id) {
        return adService.getAdById(id).orElseThrow(() -> {
            logger.error("L'annonce avec l'ID {} n'a pas été trouvée", id);
            return new ResourceNotFoundException("Ad not found");
        });
    }
}
