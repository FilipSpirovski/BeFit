package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/count")
    public Integer getNumberOfUsers() {
        return this.userService.getNumberOfUsers();
    }

    @GetMapping("/{email}/meals")
    public Map<String, Object> getMealsCreatedByUser(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size,
                                                     @PathVariable String email,
                                                     @RequestParam String text) {
        return this.userService.getMealsCreatedByUser(email, page, size, text);
    }

    @GetMapping("/{email}/workout-plans")
    public Map<String, Object> getWorkoutPlansCreatedByUser(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size,
                                                            @PathVariable String email,
                                                            @RequestParam String text) {
        return this.userService.getWorkoutPlansCreatedByUser(page, size, email, text);
    }

    @GetMapping("/favorite-meals")
    public Map<String, Object> getFavoriteMealsForUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size,
                                                       @QuerydslPredicate(root = Meal.class) Predicate predicate) {
        return this.userService.getFavoriteMealsForUser(page, size, predicate);
    }

    @GetMapping("/favorite-workout-plans")
    public Map<String, Object> getFavoriteWorkoutPlansForUser(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "3") int size,
                                                              @QuerydslPredicate(root = WorkoutPlan.class) Predicate predicate) {
        return this.userService.getFavoriteWorkoutPlansForUser(page, size, predicate);
    }
}
