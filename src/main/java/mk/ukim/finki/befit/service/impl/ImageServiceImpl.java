package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.exception.ImageNotFoundException;
import mk.ukim.finki.befit.repository.ImageRepository;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
    public Image getImage(String name) {
        try {
            Image image = this.findByName(name);

            return new Image(
                    image.getName(),
                    image.getType(),
                    decompressBytes(image.getPictureBytes())
            );
        } catch (ImageNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @Override
    public Image upload(MultipartFile imageFile) throws IOException {
        System.out.println("Original image byte size -> " + imageFile.getBytes().length);

        Image image = new Image(
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                imageFile.getBytes()
        );

        return image;
    }

    @Override
    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        deflater.setInput(data);
        deflater.finish();

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Compressed image byte size -> " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    @Override
    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        inflater.setInput(data);
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            System.out.println(ioe.getMessage());
        }

        return outputStream.toByteArray();
    }
}
