package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@CrossOrigin("http://localhost:4200")
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/all")
    public List<Exercise> getExercises() {
        return this.exerciseService.findAll();
    }

    @GetMapping("/{muscleGroup}/all")
    public List<Exercise> getExercisesByMuscleGroup(@PathVariable String muscleGroup) {
        return this.exerciseService.findAllByMuscleGroup(MuscleGroup.valueOf(muscleGroup));
    }

    @GetMapping("/search/{text}")
    public List<Exercise> searchExercisesByName(@PathVariable String text) {
        return this.exerciseService.searchByName(text);
    }
}
