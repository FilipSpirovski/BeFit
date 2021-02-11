package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.exception.ImageNotFoundException;
import mk.ukim.finki.befit.repository.ImageRepository;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image findByName(String name) {
        return this.imageRepository.findByName(name)
                .orElseThrow(() -> new ImageNotFoundException(name));
    }

    @Override
    public Image save(Image image) {
        return this.imageRepository.save(image);
    }
}
