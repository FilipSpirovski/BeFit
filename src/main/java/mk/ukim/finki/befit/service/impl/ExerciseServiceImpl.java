package mk.ukim.finki.befit.service.impl;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.Image;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.exception.ExerciseNotFoundException;
import mk.ukim.finki.befit.repository.ExerciseRepository;
import mk.ukim.finki.befit.service.ExerciseService;
import mk.ukim.finki.befit.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ImageService imageService;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ImageService imageService) {
        this.exerciseRepository = exerciseRepository;
        this.imageService = imageService;
    }

    @Override
    public Integer getNumberOfExercises() {
        return this.exerciseRepository.findAll().size();
    }

    @Override
    public Map<String, Object> findAllByPredicate(int page, int size, Predicate predicate) {
        Page<Exercise> exercisesPage;
        List<Exercise> exercises;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        exercisesPage = this.exerciseRepository.findAll(predicate, paging);
        exercises = exercisesPage.getContent();

        response.put("exercises", exercises);
        response.put("currentPage", exercisesPage.getNumber());
        response.put("totalItems", exercisesPage.getTotalElements());
        response.put("totalPages", exercisesPage.getTotalPages());

        return response;
    }

    @Override
    public List<Exercise> findAllByMuscleGroup(String muscleGroup) {
        return this.exerciseRepository.findAllByMuscleGroup(MuscleGroup.valueOf(muscleGroup));
    }

    @Override
    public List<Exercise> searchByName(String text) {
        return this.exerciseRepository.findAllByNameLike("%" + text + "%");
    }

    @Override
    public Exercise findById(Long id) {
        return this.exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @Override
    public Exercise save(String jsonExercise, MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();
        Image image = this.imageService.getImageFromFile(imageFile);
        Exercise exercise = gson.fromJson(jsonExercise, Exercise.class);

        exercise.setImage(image);
        exercise = this.exerciseRepository.save(exercise);

        return exercise;
    }
}
