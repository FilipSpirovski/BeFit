package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import mk.ukim.finki.befit.model.exception.MealNotFoundException;
import mk.ukim.finki.befit.service.ImageService;
import mk.ukim.finki.befit.service.MealService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meals")
@CrossOrigin("http://localhost:4200")
public class MealController {
    private final MealService mealService;
    private final ImageService imageService;

    public MealController(MealService mealService, ImageService imageService) {
        this.mealService = mealService;
        this.imageService = imageService;
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

    @GetMapping("/latest/{id}")
    public List<Meal> getLatestMeals(@PathVariable Long id) {
        return this.mealService.findAll()
                .stream()
                .filter(meal -> !meal.getId().equals(id))
                .sorted(Comparator.comparing(Meal::getSubmissionTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/trending/{id}")
    public List<Meal> getMostPopularMeals(@PathVariable Long id) {
        return this.mealService.findAll()
                .stream()
                .filter(meal -> !meal.getId().equals(id))
                .sorted(Comparator.comparing(Meal::getRating))
                .limit(3)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public Meal addMeal(@RequestParam("meal") String jsonMeal,
                        @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();
        Meal meal = gson.fromJson(jsonMeal, Meal.class);

        Image image = this.imageService.upload(imageFile);
        meal.setImage(image);
        meal.setSubmissionTime(LocalDateTime.now());
        meal = this.mealService.save(meal);

        return meal;
    }

    @GetMapping("/{id}")
    public Meal getMeal(@PathVariable Long id) {
        try {
            return this.mealService.findById(id);
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/add-to-favorites")
    public Meal addToFavorites(@PathVariable Long id) {
        try {
            Meal meal = this.mealService.findById(id);
            // TODO: Find the user that is currently logged in.

            return meal;
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/remove-from-favorites")
    public Meal removeFromFavorites(@PathVariable Long id) {
        try {
            Meal meal = this.mealService.findById(id);
            // TODO: Find the user that is currently logged in.

            return meal;
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
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
