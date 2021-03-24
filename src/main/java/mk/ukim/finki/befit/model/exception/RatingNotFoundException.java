package mk.ukim.finki.befit.model.exception;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(Long id) {
        super(String.format("The rating with the provided id (%d) does not exist!", id));
    }
}
