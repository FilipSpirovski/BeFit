package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.service.MealService;
import mk.ukim.finki.befit.service.ReviewService;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@CrossOrigin("http://localhost:4200")
public class ReviewController {

    private final ReviewService reviewService;
    private final WorkoutPlanService workoutPlanService;
    private final MealService mealService;

    public ReviewController(ReviewService reviewService, WorkoutPlanService workoutPlanService,
                            MealService mealService) {
        this.reviewService = reviewService;
        this.workoutPlanService = workoutPlanService;
        this.mealService = mealService;
    }

    @PostMapping("/add/workout-plan/{id}")
    public Review addReviewForWorkoutPlan(@PathVariable Long id, @RequestBody Review review) {
        return this.workoutPlanService.addReviewForWorkoutPlan(id, review);
    }

    @PostMapping("/add/meal/{id}")
    public Review addReviewForMeal(@PathVariable Long id, @RequestBody Review review) {
        return this.mealService.addReviewForMeal(id, review);
    }

    @PostMapping("/edit")
    public Review editReview(@RequestBody Review review) {
        return this.reviewService.edit(review);
    }

    @PostMapping("/{id}/delete")
    public Review deleteReview(@PathVariable Long id) {
        return this.reviewService.delete(id);
    }
}
