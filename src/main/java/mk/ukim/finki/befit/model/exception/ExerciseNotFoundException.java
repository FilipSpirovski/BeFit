package mk.ukim.finki.befit.model.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super(String.format("The exercise with the provided id (%d) does not exist!", id));
    }
}
