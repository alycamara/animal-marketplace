package com.camara.animalmarketplace.service;


import com.camara.animalmarketplace.model.ContactRequest;
import com.camara.animalmarketplace.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public List<ContactRequest> findAll() {
        return contactMessageRepository.findAll();
    }

    public ContactRequest findById(Long id) {
        return contactMessageRepository.findById(id).orElse(null);
    }

    public ContactRequest save(ContactRequest message) {
        return contactMessageRepository.save(message);
    }

    public void deleteById(Long id) {
        contactMessageRepository.deleteById(id);
    }
}
