package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Image;

public interface ImageService {
    Image findByName(String name);

    Image save(Image image);
}
