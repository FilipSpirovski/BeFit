package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.*;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workouts")
@CrossOrigin("http://localhost:4200")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/all/{criteria}")
    public Map<String, Object> getWorkoutPlans(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size,
                                               @PathVariable String criteria,
                                               @QuerydslPredicate(root = WorkoutPlan.class) Predicate predicate) {
        return this.workoutPlanService.findAllByCriteriaAndPredicate(page, size, criteria, predicate);
    }

    @GetMapping("/count")
    public Integer getNumberOfWorkoutPlans() {
        return this.workoutPlanService.getNumberOfWorkoutPlans();
    }

    @GetMapping("/latest/{currentWorkoutPlanId}")
    public List<WorkoutPlan> getLatestWorkoutPlans(@PathVariable Long currentWorkoutPlanId) {
        return this.workoutPlanService.getLatestWorkoutPlans(currentWorkoutPlanId);
    }

    @GetMapping("/trending/{currentWorkoutPlanId}")
    public List<WorkoutPlan> getMostPopularWorkoutPlans(@PathVariable Long currentWorkoutPlanId) {
        return this.workoutPlanService.getMostPopularWorkoutPlans(currentWorkoutPlanId);
    }

    @GetMapping("/{id}")
    public WorkoutPlan getWorkoutPlan(@PathVariable Long id) {
        return this.workoutPlanService.findById(id);
    }

    @PostMapping("/add")
    public WorkoutPlan addWorkoutPlan(@RequestParam("workoutPlan") String jsonWorkoutPlan,
                                      @RequestParam("imageFile") MultipartFile imageFile,
                                      Authentication authentication) throws IOException {
        return this.workoutPlanService.save(jsonWorkoutPlan, imageFile, authentication);
    }

    @PostMapping("/{id}/add-to-favorites")
    public UserDto addToFavorites(@PathVariable Long id,
                                  Authentication authentication) {
        return this.workoutPlanService.addToFavorites(id, authentication);
    }

    @PostMapping("/{id}/remove-from-favorites")
    public UserDto removeFromFavorites(@PathVariable Long id,
                                       Authentication authentication) {
        return this.workoutPlanService.removeFromFavorites(id, authentication);
    }

    @PostMapping("/edit")
    public WorkoutPlan editWorkoutPlan(@RequestParam("workoutPlan") String jsonWorkoutPlan,
                                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return this.workoutPlanService.editFromJson(jsonWorkoutPlan, imageFile);
    }

    @PostMapping("/{id}/delete")
    public WorkoutPlan deleteWorkoutPlan(@PathVariable Long id) {
        return this.workoutPlanService.delete(id);
    }
}
