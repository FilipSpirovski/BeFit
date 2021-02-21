package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workouts")
@CrossOrigin("http://localhost:4200")
public class WorkoutPlanController {
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanService workoutPlanService;
    private final ImageService imageService;
    private final ExerciseService exerciseService;

    public WorkoutPlanController(WorkoutPlanRepository workoutPlanRepository, WorkoutPlanService workoutPlanService,
                                 ImageService imageService, ExerciseService exerciseService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanService = workoutPlanService;
        this.imageService = imageService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/all")
    public Map<String, Object> getWorkoutPlans(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<WorkoutPlan> workoutPlanPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        workoutPlanPage = this.workoutPlanRepository.findAll(paging);
        workoutPlans = workoutPlanPage.getContent();
        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlanPage.getNumber());
        response.put("totalItems", workoutPlanPage.getTotalElements());
        response.put("totalPages", workoutPlanPage.getTotalPages());

        return response;
    }

    @GetMapping("/count")
    public Integer getNumberOfWorkoutPlans() {
        return this.workoutPlanService.findAll().size();
    }

    @GetMapping("/latest/{id}")
    public List<WorkoutPlan> getLatestWorkoutPlans(@PathVariable Long id) {
        return this.workoutPlanService.findAll()
                .stream()
                .filter(workoutPlan -> !workoutPlan.getId().equals(id))
                .sorted(Comparator.comparing(WorkoutPlan::getSubmissionTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/trending/{id}")
    public List<WorkoutPlan> getMostPopularWorkoutPlans(@PathVariable Long id) {
        return this.workoutPlanService.findAll()
                .stream()
                .filter(workoutPlan -> !workoutPlan.getId().equals(id))
                .sorted(Comparator.comparing(WorkoutPlan::getRating))
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WorkoutPlan getWorkoutPlan(@PathVariable Long id) {
        try {
            return this.workoutPlanService.findById(id);
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/add")
    public WorkoutPlan addWorkoutPlan(@RequestParam("workoutPlan") String jsonWorkoutPlan,
                                      @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();
        WorkoutPlan workoutPlan = gson.fromJson(jsonWorkoutPlan, WorkoutPlan.class);

        workoutPlan.getExercises()
                .forEach(exerciseWrapper -> {
                    Exercise exercise = this.exerciseService.findById(exerciseWrapper.getExerciseId());
                    exerciseWrapper.setExercise(exercise);
                });
        Image image = this.imageService.upload(imageFile);
        workoutPlan.setImage(image);
        workoutPlan.setSubmissionTime(LocalDateTime.now());
        workoutPlan = this.workoutPlanService.save(workoutPlan);

        return workoutPlan;
    }

    @PostMapping("/{id}/add-to-favorites")
    public WorkoutPlan addToFavorites(@PathVariable Long id) {
        try {
            WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
            // TODO: Find the user that is currently logged in.

            return workoutPlan;
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/remove-from-favorites")
    public WorkoutPlan removeFromFavorites(@PathVariable Long id) {
        try {
            WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
            // TODO: Find the user that is currently logged in.

            return workoutPlan;
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/edit")
    public WorkoutPlan editWorkoutPlan(@RequestBody WorkoutPlan workoutPlan) {
        try {
            return this.workoutPlanService.edit(workoutPlan);
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/delete")
    public WorkoutPlan deleteWorkoutPlan(@PathVariable Long id) {
        try {
            return this.workoutPlanService.delete(id);
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
