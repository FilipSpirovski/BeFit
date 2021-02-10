package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findAllByNameLike(String text);
}
