package com.camara.animalmarketplace.service;

import com.camara.animalmarketplace.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;


}
