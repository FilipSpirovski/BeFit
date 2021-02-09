package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@CrossOrigin("http://localhost:4200")
public class WorkoutPlanController {
    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/all")
    public List<WorkoutPlan> getWorkoutPlans() {
        return this.workoutPlanService.findAll();
    }

    @PostMapping("/add")
    public WorkoutPlan addWorkoutPlan(@RequestBody WorkoutPlan workoutPlan) {
        return this.workoutPlanService.save(workoutPlan);
    }

    @PostMapping("/{id}/favorites-add")
    public WorkoutPlan addToFavorites(@PathVariable Long id) {
        WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
        // TODO: Find the user that is currently logged in.

        return workoutPlan;
    }

    @PostMapping("/{id}/favorites-remove")
    public WorkoutPlan removeFromFavorites(@PathVariable Long id) {
        WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);
        // TODO: Find the user that is currently logged in.

        return workoutPlan;
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
