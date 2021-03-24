package mk.ukim.finki.befit.service;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MealService {

    Integer getNumberOfMeals();

    List<Meal> findAll();

    List<Meal> getLatestMeals(Long currentMealId);

    List<Meal> getMostPopularMeals(Long currentMealId);

    Page<Meal> findAllByCreator(String userEmail, Pageable pageable);

    Page<Meal> findAllByCreatorAndTitle(String userEmail, String text, Pageable pageable);

    List<Meal> findAllByMealType(String mealType);

    List<Meal> findAllByDietaryType(String dietaryType);

    Page<Meal> findAllByPredicate(Predicate predicate, Pageable pageable);

    Map<String, Object> findAllByCriteriaAndPredicate(int page, int size, String criteria, Predicate predicate);

    Meal findById(Long id);

    Meal save(String jsonMeal, MultipartFile imageFile, Authentication authentication) throws IOException;

    UserDto addToFavorites(Long id, Authentication authentication);

    Review addReviewForMeal(Long id, Review review);

    UserDto removeFromFavorites(Long id, Authentication authentication);

    Meal edit(Meal meal, boolean newImage);

    Meal editFomJson(String jsonMeal, MultipartFile imageFile) throws IOException;

    Meal delete(Long id);
}
