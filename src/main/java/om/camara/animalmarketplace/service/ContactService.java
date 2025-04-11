package om.camara.animalmarketplace.service;


import om.camara.animalmarketplace.model.ContactMessage;
import om.camara.animalmarketplace.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public List<ContactMessage> findAll() {
        return contactMessageRepository.findAll();
    }

    public ContactMessage findById(Long id) {
        return contactMessageRepository.findById(id).orElse(null);
    }

    public ContactMessage save(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    public void deleteById(Long id) {
        contactMessageRepository.deleteById(id);
    }
}
