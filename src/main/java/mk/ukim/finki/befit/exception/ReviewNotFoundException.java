package mk.ukim.finki.befit.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super(String.format("The review with the provided id (%d) does not exist!", id));
    }
}
