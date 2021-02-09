package mk.ukim.finki.befit.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(String.format("The user with the provided email (%s) does not exist!", email));
    }
}
