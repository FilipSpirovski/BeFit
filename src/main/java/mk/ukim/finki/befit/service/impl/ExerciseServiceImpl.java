package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.exception.ExerciseNotFoundException;
import mk.ukim.finki.befit.repository.ExerciseRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ImageService imageService;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository,
                               ImageService imageService) {
        this.exerciseRepository = exerciseRepository;
        this.imageService = imageService;
    }

    @Override
    public Exercise save(Exercise exercise) {
        return this.exerciseRepository.save(exercise);
    }

    public void decompressImages(List<Exercise> exercises) {
        exercises.forEach(exercise -> {
            Image image = exercise.getImage();

            try {
                exercise.setImage(this.imageService.getImage(image.getName()));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
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
    public Exercise findById(Long id) {
        return this.exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @Override
    public List<Exercise> searchByName(String text) {
        return this.exerciseRepository.findAllByNameLike("%" + text + "%");
    }
}
