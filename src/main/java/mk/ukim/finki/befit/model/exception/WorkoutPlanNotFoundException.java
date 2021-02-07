package mk.ukim.finki.befit.model.exception;

public class WorkoutPlanNotFoundException extends RuntimeException {
    public WorkoutPlanNotFoundException(Long id) {
        super(String.format("The workout plan with the provided id (%d) does not exist!", id));
    }
}
