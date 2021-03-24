package mk.ukim.finki.befit.service.impl;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final ImageService imageService;
    private final UserService userService;

    public MealServiceImpl(MealRepository mealRepository, ImageService imageService,
                           UserService userService) {
        this.mealRepository = mealRepository;
        this.imageService = imageService;
        this.userService = userService;
    }

    @Override
    public Integer getNumberOfMeals() {
        return this.findAll().size();
    }

    @Override
    public List<Meal> findAll() {
        return this.mealRepository.findAll();
    }

    @Override
    public List<Meal> getLatestMeals(Long currentMealId) {
        return this.findAll()
                .stream()
                .filter(meal -> !meal.getId().equals(currentMealId))
                .sorted(Comparator.comparing(Meal::getSubmissionTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getMostPopularMeals(Long currentMealId) {
        return this.findAll()
                .stream()
                .filter(meal -> !meal.getId().equals(currentMealId))
                .sorted(Comparator.comparing(Meal::getRating))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Meal> findAllByCreator(String userEmail, Pageable pageable) {
        return this.mealRepository.findAllByCreator(userEmail, pageable);
    }

    @Override
    public Page<Meal> findAllByCreatorAndTitle(String userEmail, String text, Pageable pageable) {
        return this.mealRepository.findAllByCreatorAndTitleLike(userEmail, "%" + text + "%", pageable);
    }

    @Override
    public List<Meal> findAllByMealType(String mealType) {
        return this.mealRepository.findAllByMealTypesContaining(MealType.valueOf(mealType));
    }

    @Override
    public List<Meal> findAllByDietaryType(String dietaryType) {
        return this.mealRepository.findAllByDietaryType(DietaryType.valueOf(dietaryType));
    }

    @Override
    public Page<Meal> findAllByPredicate(Predicate predicate, Pageable pageable) {
        return this.mealRepository.findAll(predicate, pageable);
    }

    @Override
    public Map<String, Object> findAllByCriteriaAndPredicate(int page, int size, String criteria, Predicate predicate) {
        Pageable paging;
        Page<Meal> mealsPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

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
            default:
                paging = PageRequest.of(page, size);
        }

        mealsPage = this.mealRepository.findAll(predicate, paging);
        meals = mealsPage.getContent();

        response.put("meals", meals);
        response.put("currentPage", mealsPage.getNumber());
        response.put("totalItems", mealsPage.getTotalElements());
        response.put("totalPages", mealsPage.getTotalPages());

        return response;
    }

    @Override
    public Meal findById(Long id) {
        return this.mealRepository.findById(id)
                .orElseThrow(() -> new MealNotFoundException(id));
    }

    @Override
    public Meal save(String jsonMeal, MultipartFile imageFile, Authentication authentication) throws IOException {
        Gson gson = new Gson();
        Image image = this.imageService.getImageFromFile(imageFile);
        Meal meal = gson.fromJson(jsonMeal, Meal.class);

        meal.setImage(image);
        meal.setCreator((String) authentication.getPrincipal());
        meal.setSubmissionTime(LocalDateTime.now());
        meal = this.mealRepository.save(meal);

        return meal;
    }

    @Override
    public UserDto addToFavorites(Long id, Authentication authentication) {
        Meal meal = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        user.getFavoriteMeals().add(meal);
        user = this.userService.edit(user);

        meal.getFavoriteForUsers().add(userEmail);
        this.edit(meal, false);

        return new UserDto(user);
    }

    @Override
    public Review addReviewForMeal(Long id, Review review) {
        Meal meal = this.findById(id);

        review.setSubmissionTime(LocalDateTime.now());

        meal.getReviews().add(review);
        this.edit(meal, false);

        return review;
    }

    @Override
    public UserDto removeFromFavorites(Long id, Authentication authentication) {
        Meal meal = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        user.getFavoriteMeals().remove(meal);
        user = this.userService.edit(user);

        meal.getFavoriteForUsers().remove(userEmail);
        this.edit(meal, false);

        return new UserDto(user);
    }

    @Override
    public Meal edit(Meal meal, boolean newImage) {
        Meal existingMeal = this.findById(meal.getId());

        if (newImage) {
            existingMeal.setImage(meal.getImage());
        }
        existingMeal.setTitle(meal.getTitle());
        existingMeal.setFavoriteForUsers(meal.getFavoriteForUsers());
        existingMeal.setMealTypes(meal.getMealTypes());
        existingMeal.setDietaryType(meal.getDietaryType());
        existingMeal.setPreparationTime(meal.getPreparationTime());
        existingMeal.setCookingTime(meal.getCookingTime());
        existingMeal.setServings(meal.getServings());
        existingMeal.setPrice(meal.getPrice());
        existingMeal.setDescription(meal.getDescription());
        existingMeal.setIngredients(meal.getIngredients());
        existingMeal.setPreparation(meal.getPreparation());
        existingMeal.setReviews(meal.getReviews());

        return this.mealRepository.save(existingMeal);
    }

    @Override
    public Meal editFomJson(String jsonMeal, MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();
        Meal meal = gson.fromJson(jsonMeal, Meal.class);

        if (imageFile != null) {
            Image image = this.imageService.getImageFromFile(imageFile);
            meal.setImage(image);

            return this.edit(meal, true);
        } else {
            return this.edit(meal, false);
        }
    }

    @Override
    public Meal delete(Long id) {
        Meal meal = this.findById(id);

        this.mealRepository.delete(meal);

        return meal;
    }
}
