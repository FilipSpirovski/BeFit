package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.*;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.repository.ImageRepository;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import mk.ukim.finki.befit.service.UserService;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.core.Authentication;
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
    private final ImageRepository imageRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public WorkoutPlanController(WorkoutPlanRepository workoutPlanRepository,
                                 WorkoutPlanService workoutPlanService,
                                 ImageService imageService, ImageRepository imageRepository,
                                 ExerciseService exerciseService, UserService userService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanService = workoutPlanService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @GetMapping("/all/{criteria}")
    public Map<String, Object> getWorkoutPlans(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size,
                                               @PathVariable String criteria,
                                               @QuerydslPredicate(root = WorkoutPlan.class) Predicate predicate) {
        Pageable paging;
        switch (criteria) {
            case "AlphabeticalAsc":
                paging = PageRequest.of(page, size, Sort.by("title").ascending());
                break;
            case "AlphabeticalDesc":
                paging = PageRequest.of(page, size, Sort.by("title").descending());
                break;
            case "PriceAsc":
                paging = PageRequest.of(page, size, Sort.by("price").ascending());
                break;
            case "PriceDesc":
                paging = PageRequest.of(page, size, Sort.by("price").descending());
                break;
            case "DateAsc":
                paging = PageRequest.of(page, size, Sort.by("submissionTime").ascending());
                break;
            case "DateDesc":
                paging = PageRequest.of(page, size, Sort.by("submissionTime").descending());
                break;
            default:
                paging = PageRequest.of(page, size);
        }

        Page<WorkoutPlan> workoutPlanPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        workoutPlanPage = this.workoutPlanRepository.findAll(predicate, paging);
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
                                      @RequestParam("imageFile") MultipartFile imageFile,
                                      Authentication authentication) throws IOException {
        Gson gson = new Gson();

        WorkoutPlan workoutPlan = gson.fromJson(jsonWorkoutPlan, WorkoutPlan.class);
        Image image = this.imageService.upload(imageFile);

        workoutPlan.getExercises()
                .forEach(exerciseWrapper -> {
                    Exercise exercise = this.exerciseService.findById(exerciseWrapper.getExerciseId());
                    exerciseWrapper.setExercise(exercise);
                });
        workoutPlan.setImage(image);
        workoutPlan.setCreator((String) authentication.getPrincipal());
        workoutPlan.setSubmissionTime(LocalDateTime.now());
        workoutPlan = this.workoutPlanService.save(workoutPlan);

        return workoutPlan;
    }

    @PostMapping("/{id}/add-to-favorites")
    public UserDto addToFavorites(@PathVariable Long id, Authentication authentication) {
        try {
            WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
            // TODO: Find the user that is currently logged in.
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);

            user.getFavoriteWorkoutPlans().add(workoutPlan);
            workoutPlan.getFavoriteForUsers().add(userEmail);

            return new UserDto(this.userService.edit(user));
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/remove-from-favorites")
    public UserDto removeFromFavorites(@PathVariable Long id, Authentication authentication) {
        try {
            WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
            // TODO: Find the user that is currently logged in.
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);

            user.getFavoriteWorkoutPlans().remove(workoutPlan);
            user = this.userService.edit(user);

            workoutPlan.getFavoriteForUsers().remove(userEmail);

            return new UserDto(user);
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/edit")
    public WorkoutPlan editWorkoutPlan(@RequestParam("workoutPlan") String jsonWorkoutPlan,
                                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();

        WorkoutPlan workoutPlan = gson.fromJson(jsonWorkoutPlan, WorkoutPlan.class);
        WorkoutPlan existingWorkoutPlan = this.workoutPlanService.findById(workoutPlan.getId());
        Image existingImage;

        workoutPlan.setSubmissionTime(existingWorkoutPlan.getSubmissionTime());
        workoutPlan.getExercises()
                .forEach(exerciseWrapper -> {
                    Exercise exercise = this.exerciseService.findById(exerciseWrapper.getExerciseId());
                    exerciseWrapper.setExercise(exercise);
                });

        if (imageFile != null) {
            existingImage = existingWorkoutPlan.getImage();
            this.imageRepository.deleteById(existingImage.getId());

            Image image = this.imageService.upload(imageFile);
            workoutPlan.setImage(image);

            return this.workoutPlanService.edit(workoutPlan, true);
        } else {
            return this.workoutPlanService.edit(workoutPlan, false);
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
