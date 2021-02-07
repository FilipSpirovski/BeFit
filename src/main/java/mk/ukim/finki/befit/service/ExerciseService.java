package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;

import java.util.List;

public interface ExerciseService {
    List<Exercise> findAll();

    List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> searchByName(String text);
}
