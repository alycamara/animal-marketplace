package om.camara.animalmarketplace.service;

import om.camara.animalmarketplace.exception.ResourceNotFoundException;
import om.camara.animalmarketplace.model.Photo;
import om.camara.animalmarketplace.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo updatePhoto(Long id, Photo photoDetails) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
     //   photo.setUrl(photoDetails.getUrl());
       // photo.setAd(photoDetails.getAd());
        return photoRepository.save(photo);
    }

    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
        photoRepository.delete(photo);
    }
}
