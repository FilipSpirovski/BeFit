package mk.ukim.finki.befit.web;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import mk.ukim.finki.befit.model.exception.MealNotFoundException;
import mk.ukim.finki.befit.repository.MealRepository;
import mk.ukim.finki.befit.service.ImageService;
import mk.ukim.finki.befit.service.MealService;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/meals")
@CrossOrigin("http://localhost:4200")
public class MealController {
    private final MealRepository mealRepository;
    private final MealService mealService;
    private final ImageService imageService;
    private final UserService userService;

    public MealController(MealRepository mealRepository, MealService mealService,
                          ImageService imageService, UserService userService) {
        this.mealRepository = mealRepository;
        this.mealService = mealService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("/all/{criteria}")
    public Map<String, Object> getMeals(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,
                                        @PathVariable String criteria,
                                        @QuerydslPredicate(root = Meal.class) Predicate predicate) {
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
            case "RateAsc":
                paging = PageRequest.of(page, size, Sort.by("rating").ascending());
                break;
            case "RateDesc":
                paging = PageRequest.of(page, size, Sort.by("rating").descending());
                break;
            default:
                paging = PageRequest.of(page, size);
        }
        Page<Meal> mealPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

        mealPage = this.mealRepository.findAll(predicate, paging);
        meals = mealPage.getContent();
        response.put("meals", meals);
        response.put("currentPage", mealPage.getNumber());
        response.put("totalItems", mealPage.getTotalElements());
        response.put("totalPages", mealPage.getTotalPages());

        return response;
    }

    @GetMapping("/count")
    public Integer getNumberOfMeals() {
        return this.mealService.findAll().size();
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
                        @RequestParam("imageFile") MultipartFile imageFile,
                        Authentication authentication) throws IOException {
        Gson gson = new Gson();

        Meal meal = gson.fromJson(jsonMeal, Meal.class);
        Authentication test = SecurityContextHolder.getContext().getAuthentication();
        Image image = this.imageService.upload(imageFile);
        meal.setImage(image);
        meal.setEmail((String) authentication.getPrincipal());
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
    public UserDto addToFavorites(@PathVariable Long id, Authentication authentication) {
        try {
            Meal meal = this.mealService.findById(id);
            // TODO: Find the user that is currently logged in.
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);

            user.getFavoriteMeals().add(meal);

            return new UserDto(this.userService.edit(user));
        } catch (MealNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/{id}/remove-from-favorites")
    public UserDto removeFromFavorites(@PathVariable Long id, Authentication authentication) {
        try {
            Meal meal = this.mealService.findById(id);
            // TODO: Find the user that is currently logged in.
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);

            user.getFavoriteMeals().remove(meal);

            return new UserDto(this.userService.edit(user));
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
