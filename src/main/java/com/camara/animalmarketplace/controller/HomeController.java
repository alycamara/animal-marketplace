package com.camara.animalmarketplace.controller;

import com.camara.animalmarketplace.model.Ad;
import com.camara.animalmarketplace.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private AdService adService;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String species,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) String sort,
                       Model model) {
        List<Ad> ads = adService.findAds(species, location, sort);
        model.addAttribute("ads", ads);
        return "ad/list";
    }
}

