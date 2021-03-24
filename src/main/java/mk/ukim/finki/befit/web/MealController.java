package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.service.MealService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meals")
@CrossOrigin("http://localhost:4200")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/all/{criteria}")
    public Map<String, Object> getMeals(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,
                                        @PathVariable String criteria,
                                        @QuerydslPredicate(root = Meal.class) Predicate predicate) {
        return this.mealService.findAllByCriteriaAndPredicate(page, size, criteria, predicate);
    }

    @GetMapping("/count")
    public Integer getNumberOfMeals() {
        return this.mealService.getNumberOfMeals();
    }

    @GetMapping("/{mealType}/all")
    public List<Meal> getMealsByMealType(@PathVariable String mealType) {
        return this.mealService.findAllByMealType(mealType);
    }

    @GetMapping("/{dietaryType}/all")
    public List<Meal> getMealsByDietaryType(@PathVariable String dietaryType) {
        return this.mealService.findAllByDietaryType(dietaryType);
    }

    @GetMapping("/latest/{currentMealId}")
    public List<Meal> getLatestMeals(@PathVariable Long currentMealId) {
        return this.mealService.getLatestMeals(currentMealId);
    }

    @GetMapping("/trending/{currentMealId}")
    public List<Meal> getMostPopularMeals(@PathVariable Long currentMealId) {
        return this.mealService.getMostPopularMeals(currentMealId);
    }

    @PostMapping("/add")
    public Meal addMeal(@RequestParam("meal") String jsonMeal,
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Authentication authentication) throws IOException {
        return this.mealService.save(jsonMeal, imageFile, authentication);
    }

    @GetMapping("/{id}")
    public Meal getMeal(@PathVariable Long id) {
        return this.mealService.findById(id);
    }

    @PostMapping("/{id}/add-to-favorites")
    public UserDto addToFavorites(@PathVariable Long id, Authentication authentication) {
        return this.mealService.addToFavorites(id, authentication);
    }

    @PostMapping("/{id}/remove-from-favorites")
    public UserDto removeFromFavorites(@PathVariable Long id, Authentication authentication) {
        return this.mealService.removeFromFavorites(id, authentication);
    }

    @PostMapping("/edit")
    public Meal editMeal(@RequestParam("meal") String jsonMeal,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        return this.mealService.editFomJson(jsonMeal, imageFile);
    }

    @PostMapping("/{id}/delete")
    public Meal deleteMeal(@PathVariable Long id) {
        return this.mealService.delete(id);
    }
}
