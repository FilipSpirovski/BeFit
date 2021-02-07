package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import mk.ukim.finki.befit.model.exception.MealNotFoundException;
import mk.ukim.finki.befit.service.MealService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meals")
@CrossOrigin("http://localhost:4200")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/all")
    public List<Meal> getMeals() {
        return this.mealService.findAll();
    }

    @GetMapping("/{mealType}/all")
    public List<Meal> getMealsByMealType(@PathVariable String mealType) {
        return this.mealService.findAllByMealType(MealType.valueOf(mealType));
    }

    @GetMapping("/{dietaryType}/all")
    public List<Meal> getMealsByDietaryType(@PathVariable String dietaryType) {
        return this.mealService.findAllByDietaryType(DietaryType.valueOf(dietaryType));
    }

    @PostMapping("/add")
    public Meal addMeal(@RequestBody Meal meal) {
        return this.mealService.save(meal);
    }

    @PostMapping("/{id}/favorites-add")
    public Meal addToFavorites(@PathVariable Long id) {
        Meal meal = this.mealService.findById(id);
        // TODO: Find the user that is currently logged in.

        return meal;
    }

    @PostMapping("/{id}/favorites-remove")
    public Meal removeFromFavorites(@PathVariable Long id) {
        Meal meal = this.mealService.findById(id);
        // TODO: Find the user that is currently logged in.

        return meal;
    }

    @PostMapping("/edit")
    public Meal editMeal(@RequestBody Meal meal) {
        try {
            return this.mealService.edit(meal);
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/delete")
    public Meal deleteMeal(@PathVariable Long id) {
        try {
            return this.mealService.delete(id);
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
