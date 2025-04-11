package om.camara.animalmarketplace.controller;

import om.camara.animalmarketplace.model.ContactMessage;
import om.camara.animalmarketplace.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String listMessages(Model model) {
        model.addAttribute("messages", contactService.findAll());
        return "contact";
    }

    @GetMapping("/nouveau")
    public String showCreateForm(Model model) {
        model.addAttribute("message", new ContactMessage());
        return "createContact";
    }

    @PostMapping
    public String createMessage(ContactMessage message) {
        contactService.save(message);
        return "redirect:/contact";
    }
}
