package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import mk.ukim.finki.befit.model.exception.MealNotFoundException;
import mk.ukim.finki.befit.repository.MealRepository;
import mk.ukim.finki.befit.service.MealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;

    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public List<Meal> findAll() {
        return this.mealRepository.findAll();
    }

    @Override
    public List<Meal> findAllByMealType(MealType mealType) {
        return this.mealRepository.findAllByMealTypesContaining(mealType);
    }

    @Override
    public List<Meal> findAllByDietaryType(DietaryType dietaryType) {
        return this.mealRepository.findAllByDietaryType(dietaryType);
    }

    @Override
    public Meal findById(Long id) {
        return this.mealRepository.findById(id)
                .orElseThrow(() -> new MealNotFoundException(id));
    }

    @Override
    public Meal save(Meal meal) {
        return this.mealRepository.save(meal);
    }

    @Override
    public Meal edit(Meal meal) {
        Meal existingMeal = this.findById(meal.getId());

        existingMeal.setTitle(meal.getTitle());
        existingMeal.setImage(meal.getImage());
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
    public Meal delete(Long id) {
        Meal meal = this.findById(id);

        this.mealRepository.delete(meal);

        return meal;
    }
}
