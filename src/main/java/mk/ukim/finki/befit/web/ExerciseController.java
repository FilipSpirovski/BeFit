package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.exception.ExerciseNotFoundException;
import mk.ukim.finki.befit.repository.ExerciseRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercises")
@CrossOrigin("http://localhost:4200")
public class ExerciseController {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;
    private final ImageService imageService;

    public ExerciseController(ExerciseRepository exerciseRepository,
                              ExerciseService exerciseService,
                              ImageService imageService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.imageService = imageService;
    }

    @GetMapping("/count")
    public long getNumberOfExercises() {
        return this.exerciseService.findAll().size();
    }

    @GetMapping("/all")
    public Map<String, Object> getExercises(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size,
                                            @QuerydslPredicate(root = Exercise.class) Predicate predicate) {

        Page<Exercise> exercisePage;
        List<Exercise> exercises;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);
        exercisePage = this.exerciseRepository.findAll(predicate, paging);
        exercises = exercisePage.getContent();

        response.put("exercises", exercises);
        response.put("currentPage", exercisePage.getNumber());
        response.put("totalItems", exercisePage.getTotalElements());
        response.put("totalPages", exercisePage.getTotalPages());

        return response;
    }

    @GetMapping("/{muscleGroup}/all")
    public List<Exercise> getExercisesByMuscleGroup(@PathVariable String muscleGroup) {
        return this.exerciseService.findAllByMuscleGroup(MuscleGroup.valueOf(muscleGroup));
    }

    @GetMapping("/search/{text}")
    public List<Exercise> searchExercisesByName(@PathVariable String text) {
        return this.exerciseService.searchByName(text);
    }

    @PostMapping("/add")
    public Exercise addExercise(@RequestParam("exercise") String jsonExercise,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();

        Exercise exercise = gson.fromJson(jsonExercise, Exercise.class);
        Image image = this.imageService.upload(imageFile);

        exercise.setImage(image);
        exercise = this.exerciseService.save(exercise);

        return exercise;
    }

    @GetMapping("/{id}")
    public Exercise getExercise(@PathVariable Long id) {
        try {
            return this.exerciseService.findById(id);
        } catch (ExerciseNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
