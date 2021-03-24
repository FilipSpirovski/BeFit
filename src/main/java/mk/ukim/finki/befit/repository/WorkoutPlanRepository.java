package mk.ukim.finki.befit.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import mk.ukim.finki.befit.model.QWorkoutPlan;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
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
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long>,
        QuerydslPredicateExecutor<WorkoutPlan>, QuerydslBinderCustomizer<QWorkoutPlan> {

    Page<WorkoutPlan> findAllByCreator(String email, Pageable pageable);

    Page<WorkoutPlan> findAllByCreatorAndTitleLike(String email, String text, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QWorkoutPlan workoutPlan) {
        bindings.bind(workoutPlan.title).first((path, value) -> {
            if (!value.isEmpty()) {
                return path.toLowerCase().contains(value.toLowerCase());
            } else {
                return Expressions.asBoolean(true).isFalse();
            }
        });

        bindings.bind(workoutPlan.workoutType).first((path, value) -> path.eq(value));

        bindings.bind(workoutPlan.equipment).first((path, value) -> path.eq(value));

        bindings.bind(workoutPlan.bodyPart).first((path, value) -> path.eq(value));

        bindings.bind(workoutPlan.muscleGroups).all((path, value) -> {
            List<MuscleGroup> muscleGroupsList = new ArrayList<>();
            for (List<MuscleGroup> list : value) {
                muscleGroupsList.add(list.get(0));
            }
            BooleanBuilder predicate = new BooleanBuilder();
            muscleGroupsList.forEach(muscleGroup -> predicate.or(path.contains(muscleGroup)));
            return Optional.of(predicate);
        });

        bindings.bind(workoutPlan.favoriteForUsers).first((path, value) -> path.contains(value.get(0)));
    }
}
