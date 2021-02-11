package mk.ukim.finki.befit.model.exception;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException(String name) {
        super(String.format("The image with the provided name (%s) does not exist!", name));
    }
}
