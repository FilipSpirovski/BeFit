package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.exception.ImageNotFoundException;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@RequestMapping("/images")
@CrossOrigin("http://localhost:4200")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity.BodyBuilder uploadImage(@RequestBody MultipartFile imageFile) throws IOException {
        System.out.println("Original image byte size -> " + imageFile.getBytes().length);
        Image image = new Image(
                imageFile.getOriginalFilename(),
                imageFile.getContentType(),
                compressBytes(imageFile.getBytes())
        );

        this.imageService.save(image);

        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping("/{imageName}")
    public Image getImage(@PathVariable String imageName) throws IOException {
        try {
            Image image = this.imageService.findByName(imageName);

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

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        deflater.setInput(data);
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

    public static byte[] decompressBytes(byte[] data) {
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
