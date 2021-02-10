package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.enumeration.BodyPart;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findAllByEmail(String email);

    List<WorkoutPlan> findAllByWorkoutType(WorkoutType workoutType);

    List<WorkoutPlan> findAllByEquipment(Boolean equipment);

    List<WorkoutPlan> findAllByBodyPart(BodyPart bodyPart);

    List<WorkoutPlan> findAllByMuscleGroupsContaining(MuscleGroup muscleGroup);
}
