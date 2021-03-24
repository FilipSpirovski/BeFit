package mk.ukim.finki.befit.service.impl;

import com.google.gson.Gson;
import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.*;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ImageService imageService;
    private final ExerciseService exerciseService;
    private final UserService userService;
    private final ReviewService reviewService;

    public WorkoutPlanServiceImpl(WorkoutPlanRepository workoutPlanRepository, ImageService imageService,
                                  ExerciseService exerciseService, UserService userService,
                                  ReviewService reviewService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.imageService = imageService;
        this.exerciseService = exerciseService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @Override
    public Integer getNumberOfWorkoutPlans() {
        return this.findAll().size();
    }

    @Override
    public List<WorkoutPlan> findAll() {
        return this.workoutPlanRepository.findAll();
    }

    @Override
    public List<WorkoutPlan> getLatestWorkoutPlans(Long currentWorkoutPlanId) {
        return this.findAll()
                .stream()
                .filter(workoutPlan -> !workoutPlan.getId().equals(currentWorkoutPlanId))
                .sorted(Comparator.comparing(WorkoutPlan::getSubmissionTime))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkoutPlan> getMostPopularWorkoutPlans(Long currentWorkoutPlanId) {
        return this.findAll()
                .stream()
                .filter(workoutPlan -> !workoutPlan.getId().equals(currentWorkoutPlanId))
                .sorted(Comparator.comparing(WorkoutPlan::getRating))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Page<WorkoutPlan> findAllByCreator(String userEmail, Pageable pageable) {
        return this.workoutPlanRepository.findAllByCreator(userEmail, pageable);
    }

    @Override
    public Page<WorkoutPlan> findAllByCreatorAndTitle(String userEmail, String text, Pageable pageable) {
        return this.workoutPlanRepository.findAllByCreatorAndTitleLike(userEmail, "%" + text + "%", pageable);
    }

    @Override
    public Map<String, Object> findAllByCriteriaAndPredicate(int page, int size, String criteria, Predicate predicate) {
        Pageable paging;
        Page<WorkoutPlan> workoutPlansPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        switch (criteria) {
            case "AlphabeticalAsc":
                paging = PageRequest.of(page, size, Sort.by("title").ascending());
                break;
            case "AlphabeticalDesc":
                paging = PageRequest.of(page, size, Sort.by("title").descending());
                break;
            case "PriceAsc":
                paging = PageRequest.of(page, size, Sort.by("price").ascending());
                break;
            case "PriceDesc":
                paging = PageRequest.of(page, size, Sort.by("price").descending());
                break;
            case "DateAsc":
                paging = PageRequest.of(page, size, Sort.by("submissionTime").ascending());
                break;
            case "DateDesc":
                paging = PageRequest.of(page, size, Sort.by("submissionTime").descending());
                break;
            default:
                paging = PageRequest.of(page, size);
        }

        workoutPlansPage = this.workoutPlanRepository.findAll(predicate, paging);
        workoutPlans = workoutPlansPage.getContent();

        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlansPage.getNumber());
        response.put("totalItems", workoutPlansPage.getTotalElements());
        response.put("totalPages", workoutPlansPage.getTotalPages());

        return response;
    }

    @Override
    public Page<WorkoutPlan> findAllByPredicate(Predicate predicate, Pageable pageable) {
        return this.workoutPlanRepository.findAll(predicate, pageable);
    }

    @Override
    public WorkoutPlan findById(Long id) {
        return this.workoutPlanRepository.findById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(id));
    }

    @Override
    public WorkoutPlan save(String jsonWorkoutPlan, MultipartFile imageFile, Authentication authentication) throws IOException {
        Gson gson = new Gson();
        Image image = this.imageService.getImageFromFile(imageFile);
        WorkoutPlan workoutPlan = gson.fromJson(jsonWorkoutPlan, WorkoutPlan.class);

        workoutPlan.getExercises()
                .forEach(exerciseWrapper -> {
                    Exercise exercise = this.exerciseService.findById(exerciseWrapper.getExerciseId());
                    exerciseWrapper.setExercise(exercise);
                });
        workoutPlan.setImage(image);
        workoutPlan.setCreator((String) authentication.getPrincipal());
        workoutPlan.setSubmissionTime(LocalDateTime.now());
        workoutPlan = this.workoutPlanRepository.save(workoutPlan);

        return workoutPlan;
    }

    @Override
    public UserDto addToFavorites(Long id, Authentication authentication) {
        WorkoutPlan workoutPlan = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        user.getFavoriteWorkoutPlans().add(workoutPlan);
        user = this.userService.edit(user);

        workoutPlan.getFavoriteForUsers().add(userEmail);
        this.edit(workoutPlan, false);

        return new UserDto(user);
    }

    @Override
    public Review addReviewForWorkoutPlan(Long id, Review review) {
        WorkoutPlan workoutPlan = this.findById(id);

        review.setSubmissionTime(LocalDateTime.now());

        workoutPlan.getReviews().add(review);
        this.edit(workoutPlan, false);

        return review;
    }

    @Override
    public UserDto removeFromFavorites(Long id, Authentication authentication) {
        WorkoutPlan workoutPlan = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        user.getFavoriteWorkoutPlans().remove(workoutPlan);
        user = this.userService.edit(user);

        workoutPlan.getFavoriteForUsers().remove(userEmail);
        this.edit(workoutPlan, false);

        return new UserDto(user);
    }

    @Override
    public WorkoutPlan edit(WorkoutPlan workoutPlan, boolean newImage) {
        WorkoutPlan existingWorkoutPlan = this.findById(workoutPlan.getId());

        if (newImage) {
            existingWorkoutPlan.setImage(workoutPlan.getImage());
        }
        existingWorkoutPlan.setFavoriteForUsers(workoutPlan.getFavoriteForUsers());
        existingWorkoutPlan.setTitle(workoutPlan.getTitle());
        existingWorkoutPlan.setDescription(workoutPlan.getDescription());
        existingWorkoutPlan.setWorkoutType(workoutPlan.getWorkoutType());
        existingWorkoutPlan.setEquipment((workoutPlan.getEquipment()));
        existingWorkoutPlan.setBodyPart(workoutPlan.getBodyPart());
        existingWorkoutPlan.setMuscleGroups(workoutPlan.getMuscleGroups());
        existingWorkoutPlan.setExercises(workoutPlan.getExercises());
        existingWorkoutPlan.setReviews(workoutPlan.getReviews());
        existingWorkoutPlan.setPrice(workoutPlan.getPrice());

        return this.workoutPlanRepository.save(existingWorkoutPlan);
    }

    @Override
    public WorkoutPlan editFromJson(String jsonWorkoutPlan, MultipartFile imageFile) throws IOException {
        Gson gson = new Gson();
        WorkoutPlan workoutPlan = gson.fromJson(jsonWorkoutPlan, WorkoutPlan.class);

        workoutPlan.getExercises()
                .forEach(exerciseWrapper -> {
                    Exercise exercise = this.exerciseService.findById(exerciseWrapper.getExerciseId());
                    exerciseWrapper.setExercise(exercise);
                });

        if (imageFile != null) {
            Image image = this.imageService.getImageFromFile(imageFile);
            workoutPlan.setImage(image);

            return this.edit(workoutPlan, true);
        } else {
            return this.edit(workoutPlan, false);
        }
    }

    @Override
    public WorkoutPlan delete(Long id) {
        WorkoutPlan workoutPlan = this.findById(id);

        workoutPlan.getReviews().forEach(review -> this.reviewService.delete(review.getId()));
        this.workoutPlanRepository.delete(workoutPlan);

        return workoutPlan;
    }
}
