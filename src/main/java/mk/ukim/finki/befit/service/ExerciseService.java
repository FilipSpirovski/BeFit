package mk.ukim.finki.befit.service;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Exercise;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExerciseService {

    Integer getNumberOfExercises();

    Map<String, Object> findAllByPredicate(int page, int size, Predicate predicate);

    List<Exercise> findAllByMuscleGroup(String muscleGroup);

    List<Exercise> searchByName(String text);

    Exercise findById(Long id);

    Exercise save(String jsonExercise, MultipartFile imageFile) throws IOException;
}
