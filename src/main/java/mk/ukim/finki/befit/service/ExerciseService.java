package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;

import java.util.List;

public interface ExerciseService {
    Exercise save(Exercise exercise);

    List<Exercise> findAll();

    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);

    Exercise findById(Long id);

    List<Exercise> searchByName(String text);
}
