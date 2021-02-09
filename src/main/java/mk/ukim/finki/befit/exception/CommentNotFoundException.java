package mk.ukim.finki.befit.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super(String.format("The comment with the provided id (%d) does not exist!", id));
    }
}
