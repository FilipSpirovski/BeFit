package mk.ukim.finki.befit.model.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super(String.format("The user with the provided email (%s) already exists!", email));
    }
}
