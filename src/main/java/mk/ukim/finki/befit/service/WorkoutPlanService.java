package mk.ukim.finki.befit.service;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WorkoutPlanService {

    Integer getNumberOfWorkoutPlans();

    List<WorkoutPlan> findAll();

    List<WorkoutPlan> getLatestWorkoutPlans(Long currentWorkoutPlanId);

    List<WorkoutPlan> getMostPopularWorkoutPlans(Long currentWorkoutPlanId);

    Page<WorkoutPlan> findAllByCreator(String userEmail, Pageable pageable);

    Page<WorkoutPlan> findAllByCreatorAndTitle(String userEmail, String text, Pageable pageable);

    Map<String, Object> findAllByCriteriaAndPredicate(int page, int size, String criteria, Predicate predicate);

    Page<WorkoutPlan> findAllByPredicate(Predicate predicate, Pageable pageable);

    WorkoutPlan findById(Long id);

    WorkoutPlan save(String jsonWorkoutPlan, MultipartFile imageFile, Authentication authentication) throws IOException;

    UserDto addToFavorites(Long id, Authentication authentication);

    Review addReviewForWorkoutPlan(Long id, Review review);

    UserDto removeFromFavorites(Long id, Authentication authentication);

    WorkoutPlan edit(WorkoutPlan workoutPlan, boolean newImage);

    WorkoutPlan editFromJson(String jsonWorkoutPlan, MultipartFile imageFile) throws IOException;

    WorkoutPlan delete(Long id);
}
