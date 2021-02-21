package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findAll(Pageable pageable);

    Page<Exercise> findAllByMuscleGroupAndWorkoutTypeAndEquipment(
            MuscleGroup muscleGroup, WorkoutType workoutType, Boolean equipment, Pageable pageable
    );

    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findAllByNameLike(String text);
}
