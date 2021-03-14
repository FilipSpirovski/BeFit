package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    Image findByName(String name);

    Image getImage(String name) throws IOException;

    Image upload(MultipartFile imageFile) throws IOException;

    byte[] compressBytes(byte[] data);

    byte[] decompressBytes(byte[] data);
}
