package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.exception.MealNotFoundException;
import mk.ukim.finki.befit.model.exception.ReviewNotFoundException;
import mk.ukim.finki.befit.model.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.service.MealService;
import mk.ukim.finki.befit.service.ReviewService;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reviews")
@CrossOrigin("http://localhost:4200")
public class ReviewController {
    private final ReviewService reviewService;
    private final WorkoutPlanService workoutPlanService;
    private final MealService mealService;

    public ReviewController(ReviewService reviewService,
                            WorkoutPlanService workoutPlanService,
                            MealService mealService) {
        this.reviewService = reviewService;
        this.workoutPlanService = workoutPlanService;
        this.mealService = mealService;
    }

    @PostMapping("/add/workout-plan/{id}")
    public Review addReviewForWorkoutPlan(@PathVariable Long id, @RequestBody Review review) {
        try {
            WorkoutPlan workoutPlan = this.workoutPlanService.findById(id);

            review.setSubmissionTime(LocalDateTime.now());

            workoutPlan.getReviews().add(review);
            this.workoutPlanService.edit(workoutPlan, false);

            return review;
        } catch (WorkoutPlanNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/add/meal/{id}")
    public Review addReviewForMeal(@PathVariable Long id, @RequestBody Review review) {
        try {
            Meal meal = this.mealService.findById(id);

            review.setSubmissionTime(LocalDateTime.now());

            meal.getReviews().add(review);
            this.mealService.edit(meal);

            return review;
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/edit")
    public Review editReview(@RequestBody Review review) {
        try {
            return this.reviewService.edit(review);
        } catch (ReviewNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/delete")
    public Review deleteReview(@RequestBody Review review) {
        try {
            return this.reviewService.delete(review.getId());
        } catch (ReviewNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
