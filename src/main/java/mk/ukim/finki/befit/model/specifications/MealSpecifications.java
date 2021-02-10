package mk.ukim.finki.befit.model.specifications;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.util.List;

public class MealSpecifications {

    public static Specification<Meal> mealTypesContaining(MealType mealType) {
        return (root, query, cb) -> {
            if (mealType == null) {
                return cb.conjunction();
            } else {
                Expression<List<MealType>> mealTypesList = root.get("meal").get("mealTypes");

                return cb.isMember(mealType, mealTypesList);
            }
        };
    }

    public static Specification<Meal> dietaryTypeEquals(DietaryType dietaryType) {
        return (root, query, builder) ->
                dietaryType == null ?
                        builder.conjunction() :
                        builder.equal(root.get("meal").get("dietaryType"), dietaryType);
    }

    public static Specification<Meal> preparationTimeLessThanOrEqual(Integer preparationTime) {
        return (root, query, builder) ->
                preparationTime == null ?
                        builder.conjunction() :
                        builder.lessThanOrEqualTo(root.get("preparationTime"), preparationTime);
    }

    public static Specification<Meal> preparationTimeBetween(Integer preparationTime1, Integer preparationTime2) {
        return (root, query, builder) ->
                preparationTime1 == null || preparationTime2 == null ?
                        builder.conjunction() :
                        builder.between(root.get("preparationTime"), preparationTime1, preparationTime2);
    }

    public static Specification<Meal> preparationTimeGreaterThanOrEqual(Integer preparationTime) {
        return (root, query, builder) ->
                preparationTime == null ?
                        builder.conjunction() :
                        builder.greaterThanOrEqualTo(root.get("preparationTime"), preparationTime);
    }

    public static Specification<Meal> cookingTimeLessThanOrEqual(Integer cookingTime) {
        return (root, query, builder) ->
                cookingTime == null ?
                        builder.conjunction() :
                        builder.lessThanOrEqualTo(root.get("cookingTime"), cookingTime);
    }

    public static Specification<Meal> cookingTimeBetween(Integer cookingTime1, Integer cookingTime2) {
        return (root, query, builder) ->
                cookingTime1 == null || cookingTime2 == null ?
                        builder.conjunction() :
                        builder.between(root.get("cookingTime"), cookingTime1, cookingTime2);
    }

    public static Specification<Meal> cookingTimeGreaterThanOrEqual(Integer cookingTime) {
        return (root, query, builder) ->
                cookingTime == null ?
                        builder.conjunction() :
                        builder.greaterThanOrEqualTo(root.get("cookingTime"), cookingTime);
    }

    public static Specification<Meal> servingsEqual(Integer servings) {
        return (root, query, builder) ->
                servings == null ?
                        builder.conjunction() :
                        builder.equal(root.get("servings"), servings);
    }
}
