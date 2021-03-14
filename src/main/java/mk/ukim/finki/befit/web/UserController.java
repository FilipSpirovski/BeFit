package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.repository.MealRepository;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {
    private final UserService userService;
    private final MealRepository mealRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public UserController(UserService userService,
                          MealRepository mealRepository,
                          WorkoutPlanRepository workoutPlanRepository) {
        this.userService = userService;
        this.mealRepository = mealRepository;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @GetMapping("/count")
    public Integer getNumberOfUsers() {
        return this.userService.getNumberOfUsers();
    }

    @GetMapping("/{email}/meals")
    public Map<String, Object> getMealsCreatedByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size,
                                                     @PathVariable String email, @RequestParam String text) {
        Page<Meal> mealsPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        if (text == null || text.isEmpty() || text.equals("none")) {
            mealsPage = this.mealRepository.findAllByCreator(email, paging);
        } else {
            mealsPage = this.mealRepository.findAllByCreatorAndTitleLike(email, "%" + text + "%", paging);
        }
        meals = mealsPage.getContent();

        response.put("meals", meals);
        response.put("currentPage", mealsPage.getNumber());
        response.put("totalItems", mealsPage.getTotalElements());
        response.put("totalPages", mealsPage.getTotalPages());

        return response;
    }

    @GetMapping("/{email}/workout-plans")
    public Map<String, Object> getWorkoutPlansCreatedByUser(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size,
                                                            @PathVariable String email, @RequestParam String text) {
        Page<WorkoutPlan> workoutPlansPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        if (text == null || text.isEmpty()) {
            workoutPlansPage = this.workoutPlanRepository.findAllByCreator(email, paging);
        } else {
            workoutPlansPage = this.workoutPlanRepository.findAllByCreatorAndTitleLike(email, "%" + text + "%", paging);
        }
        workoutPlans = workoutPlansPage.getContent();

        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlansPage.getNumber());
        response.put("totalItems", workoutPlansPage.getTotalElements());
        response.put("totalPages", workoutPlansPage.getTotalPages());

        return response;
    }

    @GetMapping("/favorite-meals")
    public Map<String, Object> getFavoriteMealsForUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size,
                                                       @QuerydslPredicate(root = Meal.class) Predicate predicate) {
        Page<Meal> mealsPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        mealsPage = this.mealRepository.findAll(predicate, paging);
        meals = mealsPage.getContent();

        response.put("meals", meals);
        response.put("currentPage", mealsPage.getNumber());
        response.put("totalItems", mealsPage.getTotalElements());
        response.put("totalPages", mealsPage.getTotalPages());

        return response;
    }

    @GetMapping("/favorite-workout-plans")
    public Map<String, Object> getFavoriteWorkoutPlansForUser(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              @QuerydslPredicate(root = WorkoutPlan.class) Predicate predicate) {
        Page<WorkoutPlan> workoutPlansPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        workoutPlansPage = this.workoutPlanRepository.findAll(predicate, paging);
        workoutPlans = workoutPlansPage.getContent();

        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlansPage.getNumber());
        response.put("totalItems", workoutPlansPage.getTotalElements());
        response.put("totalPages", workoutPlansPage.getTotalPages());

        return response;
    }
}
