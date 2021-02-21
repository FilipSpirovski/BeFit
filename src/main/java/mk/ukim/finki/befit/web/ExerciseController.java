package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;
import mk.ukim.finki.befit.model.exception.ExerciseNotFoundException;
import mk.ukim.finki.befit.repository.ExerciseRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exercises")
@CrossOrigin("http://localhost:4200")
public class ExerciseController {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;
    private final ImageService imageService;

    public ExerciseController(ExerciseRepository exerciseRepository, ExerciseService exerciseService, ImageService imageService) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
        this.imageService = imageService;
    }

    @GetMapping("/count")
    public long getNumberOfExercises() {
        return this.exerciseService.findAll().size();
    }

    @GetMapping("/all")
    public Map<String, Object> getExercises(@RequestParam(required = false) String workoutType,
                                            @RequestParam(required = false) List<String> muscleGroups,
                                            @RequestParam(required = false) String equipment,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "3") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Exercise> exercisePage;
        List<Exercise> exercises;
        Map<String, Object> response = new HashMap<>();

        exercisePage = this.exerciseRepository.findAll(paging);
        exercises = exercisePage.getContent();

        if (!workoutType.equals("null")) {
            exercises = exercises.stream()
                    .filter(exercise -> exercise.getWorkoutType().equals(WorkoutType.valueOf(workoutType)))
                    .collect(Collectors.toList());
        }

        if (!muscleGroups.get(0).equals("null")) {
            exercises = exercises.stream()
                    .filter(exercise -> muscleGroups.contains(exercise.getMuscleGroup().toString()))
                    .collect(Collectors.toList());
        }

        if (!equipment.equals("null")) {
            if (Boolean.parseBoolean(equipment)) {
                exercises = exercises.stream()
                        .filter(Exercise::getEquipment)
                        .collect(Collectors.toList());
            } else {
                exercises = exercises.stream()
                        .filter(exercise -> !exercise.getEquipment())
                        .collect(Collectors.toList());
            }
        }

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
