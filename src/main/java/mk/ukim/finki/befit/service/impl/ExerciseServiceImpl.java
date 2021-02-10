package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.repository.ExerciseRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> findAll() {
        return this.exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> findAllByMuscleGroup(MuscleGroup muscleGroup) {
        return this.exerciseRepository.findAllByMuscleGroup(muscleGroup);
    }

    @Override
    public List<Exercise> searchByName(String text) {
        return this.exerciseRepository.findAllByNameLike("%" + text + "%");
    }
}
