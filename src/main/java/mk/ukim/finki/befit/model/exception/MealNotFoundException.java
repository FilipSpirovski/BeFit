package mk.ukim.finki.befit.model.exception;

public class MealNotFoundException extends RuntimeException {
    public MealNotFoundException(Long id) {
        super(String.format("The meal with the provided id (%d) does not exist!", id));
    }
}
