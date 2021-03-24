package mk.ukim.finki.befit.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.QMeal;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long>,
        QuerydslPredicateExecutor<Meal>, QuerydslBinderCustomizer<QMeal> {

    List<Meal> findAllByMealTypesContaining(MealType mealType);

    List<Meal> findAllByDietaryType(DietaryType dietaryType);

    Page<Meal> findAllByCreator(String email, Pageable pageable);

    Page<Meal> findAllByCreatorAndTitleLike(String email, String title, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QMeal meal) {
        bindings.bind(meal.title).first((path, value) -> {
            if (!value.isEmpty()) {
                return path.toLowerCase().contains(value.toLowerCase());
            } else {
                return Expressions.asBoolean(true).isFalse();
            }
        });

        bindings.bind(meal.mealTypes).all((path, value) -> {
            List<MealType> mealTypesList = new ArrayList<>();
            for (List<MealType> list : value) {
                mealTypesList.add(list.get(0));
            }
            BooleanBuilder predicate = new BooleanBuilder();
            mealTypesList.forEach(mealType -> predicate.or(path.contains(mealType)));
            return Optional.of(predicate);
        });

        bindings.bind(meal.dietaryType).first((path, value) -> path.eq(value));

        bindings.bind(meal.preparationTime).all((path, value) -> {
            List<? extends Integer> preparationTimes = new ArrayList<>(value);
            Integer from = preparationTimes.get(0);
            Integer to = preparationTimes.get(1);
            return Optional.of(path.between(from, to));
        });

        bindings.bind(meal.cookingTime).all((path, value) -> {
            List<? extends Integer> cookingTimes = new ArrayList<>(value);
            Integer from = cookingTimes.get(0);
            Integer to = cookingTimes.get(1);
            return Optional.of(path.between(from, to));
        });

        bindings.bind(meal.servings).first((path, value) -> path.eq(value));

        bindings.bind(meal.favoriteForUsers).first((path, value) -> path.contains(value.get(0)));
    }
}
