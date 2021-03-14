package mk.ukim.finki.befit.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.QExercise;
import mk.ukim.finki.befit.model.QMeal;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>,
        QuerydslPredicateExecutor<Exercise>, QuerydslBinderCustomizer<QExercise> {

    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findAllByNameLike(String text);

    @Override
    default void customize(QuerydslBindings bindings, QExercise exercise) {
        bindings.bind(exercise.id).all((path, value) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            value.forEach(id -> predicate.and(path.ne(id)));
            return Optional.of(predicate);
        });

        bindings.bind(exercise.workoutType).first((path, value) -> path.eq(value));

        bindings.bind(exercise.muscleGroup).all((path, value) -> {
            BooleanBuilder predicate = new BooleanBuilder();
            value.forEach(muscleGroup -> predicate.or(path.eq(muscleGroup)));
            return Optional.of(predicate);
        });

        bindings.bind(exercise.equipment).first((path, value) -> path.eq(value));
    }
}
