package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long>, JpaSpecificationExecutor<Meal> {

}
