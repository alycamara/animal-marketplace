package com.camara.animalmarketplace.controller;

import com.camara.animalmarketplace.exception.ResourceNotFoundException;
import com.camara.animalmarketplace.model.Ad;
import com.camara.animalmarketplace.service.AdService;
import com.camara.animalmarketplace.service.S3Service;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ads")
public class AdController {

    private static final Logger logger = LoggerFactory.getLogger(AdController.class);

    private final AdService adService;
    private final S3Service s3Service;

    public AdController(AdService adService, S3Service s3Service) {

        this.adService = adService;
        this.s3Service = s3Service;
    }

    @GetMapping()
    public String getAllAds(@RequestParam(required = false) String species,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String sort,
                            Model model) {
        List<Ad> ads = adService.findAds(species, location, sort);
        model.addAttribute("ads", ads);
        return "public/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("ad", new Ad());
        return "private/create";
    }

    @GetMapping("/{id}")
    public String getAdById(@PathVariable Long id, Model model) {
        Ad ad = adService.getAdById(id).orElseThrow(() -> new ResourceNotFoundException("Ad not found"));
        model.addAttribute("ad", ad);
        return "public/detail";
    }


    @PostMapping
    public String handleCreateAd(@Valid @ModelAttribute Ad ad, BindingResult result, RedirectAttributes redirectAttributes, @RequestParam("uploadedPhotos") MultipartFile[] files) {

        logger.info("Création de l'annonce : {}", ad);
        if (result.hasErrors()) {
            logger.error("Erreur lors de la création de l'annonce : {}", result.getAllErrors());
            return "private/create"; // Retourne le formulaire avec les erreurs
        }

        adService.createAd(ad, files); // Sauvegarde de l'annonce
        redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été créée avec succès !");

        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Ad ad = findAdByIdOrThrow(id);
        model.addAttribute("ad", ad);
        return "private/edit";
    }

    @PostMapping("/update/{id}")
    public String handleUpdateAd(@PathVariable Long id,
                                 @ModelAttribute Ad adDetails,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam("uploadedPhotos") MultipartFile[] files,
                                 BindingResult result,
                                 @RequestParam(value = "deletePhotos", required = false) List<Long> deletePhotoIds) {
        if (result.hasErrors()) {
            logger.error("Erreur lors de la mis à jour de l'annonce : {}", result.getAllErrors());
            return "/update/{id}"; // Retourne le formulaire avec les erreurs
        }

        adService.updateAd(id, adDetails, files, deletePhotoIds); // Mise à jour de l'annonce
        ; // Sauvegarde de l'annonce
        redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été mis à jour avec succès !");

        return "redirect:/ads"; // Redirection vers la liste des annonces
    }

    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable Long id, Model model) {
        Ad ad = findAdByIdOrThrow(id);
        model.addAttribute("ad", ad);
        return "private/delete";
    }

    @PostMapping("/delete/{id}")
    public String handleDeleteAd(@PathVariable Long id, @ModelAttribute Ad adDetails, RedirectAttributes redirectAttributes) {

        adService.deleteAd(id);
        redirectAttributes.addFlashAttribute("successMessage", "L'annonce a été supprimée avec succès !");

        return "redirect:/ads";
    }

    private Ad findAdByIdOrThrow(Long id) {
        return adService.getAdById(id).orElseThrow(() -> {
            logger.error("L'annonce avec l'ID {} n'a pas été trouvée", id);
            return new ResourceNotFoundException("Ad not found");
        });
    }
}
