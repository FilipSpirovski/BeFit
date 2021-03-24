package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.service.ExerciseService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercises")
@CrossOrigin("http://localhost:4200")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/count")
    public Integer getNumberOfExercises() {
        return this.exerciseService.getNumberOfExercises();
    }

    @GetMapping("/all")
    public Map<String, Object> getExercises(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size,
                                            @QuerydslPredicate(root = Exercise.class) Predicate predicate) {
        return this.exerciseService.findAllByPredicate(page, size, predicate);
    }

    @GetMapping("/{muscleGroup}/all")
    public List<Exercise> getExercisesByMuscleGroup(@PathVariable String muscleGroup) {
        return this.exerciseService.findAllByMuscleGroup(muscleGroup);
    }

    @GetMapping("/search/{text}")
    public List<Exercise> searchExercisesByName(@PathVariable String text) {
        return this.exerciseService.searchByName(text);
    }

    @PostMapping("/add")
    public Exercise addExercise(@RequestParam("exercise") String jsonExercise,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        return this.exerciseService.save(jsonExercise, imageFile);
    }

    @GetMapping("/{id}")
    public Exercise getExercise(@PathVariable Long id) {
        return this.exerciseService.findById(id);
    }
}
