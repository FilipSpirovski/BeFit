package mk.ukim.finki.befit.model.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("Invalid password!");
    }
}
