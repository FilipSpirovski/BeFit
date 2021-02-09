package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.enumeration.DietaryType;
import mk.ukim.finki.befit.enumeration.MealType;

import java.util.List;

public interface MealService {
    List<Meal> findAll();

    List<Meal> findAllByMealType(MealType mealType);

    List<Meal> findAllByDietaryType(DietaryType dietaryType);

    Meal findById(Long id);

    Meal save(Meal meal);

    Meal edit(Meal meal);

    Meal delete(Long id);
}
