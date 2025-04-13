package com.camara.animalmarketplace.repository;



import com.camara.animalmarketplace.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactRequest, Long> {
}