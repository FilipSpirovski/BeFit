package mk.ukim.finki.befit.service.impl;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.exception.UserNotFoundException;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.repository.MealRepository;
import mk.ukim.finki.befit.repository.UserRepository;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public UserServiceImpl(UserRepository userRepository, MealRepository mealRepository,
                           WorkoutPlanRepository workoutPlanRepository) {
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    @Override
    public Integer getNumberOfUsers() {
        return this.userRepository.findAll().size();
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User register(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User edit(User user) {
        User existingUser = this.findByEmail(user.getEmail());

        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setRole(user.getRole());
        existingUser.setProfilePictureUrl(user.getProfilePictureUrl());
        existingUser.setFavoriteWorkoutPlans(user.getFavoriteWorkoutPlans());
        existingUser.setFavoriteMeals(user.getFavoriteMeals());
        existingUser.setLikedComments(user.getLikedComments());

        return this.userRepository.save(existingUser);
    }

    @Override
    public Map<String, Object> getMealsCreatedByUser(String userEmail, int page, int size, String text) {
        Page<Meal> mealsPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        if (text == null || text.isEmpty() || text.equals("none")) {
            mealsPage = this.mealRepository.findAllByCreator(userEmail, paging);
        } else {
            mealsPage = this.mealRepository.findAllByCreatorAndTitleLike(userEmail, "%" + text + "%", paging);
        }

        meals = mealsPage.getContent();

        response.put("meals", meals);
        response.put("currentPage", mealsPage.getNumber());
        response.put("totalItems", mealsPage.getTotalElements());
        response.put("totalPages", mealsPage.getTotalPages());

        return response;
    }

    @Override
    public Map<String, Object> getFavoriteMealsForUser(int page, int size, Predicate predicate) {
        Page<Meal> mealsPage;
        List<Meal> meals;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        mealsPage = this.mealRepository.findAll(predicate, paging);
        meals = mealsPage.getContent();

        response.put("meals", meals);
        response.put("currentPage", mealsPage.getNumber());
        response.put("totalItems", mealsPage.getTotalElements());
        response.put("totalPages", mealsPage.getTotalPages());

        return response;
    }

    @Override
    public Map<String, Object> getWorkoutPlansCreatedByUser(int page, int size, String email, String text) {
        Page<WorkoutPlan> workoutPlansPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        if (text == null || text.isEmpty()) {
            workoutPlansPage = this.workoutPlanRepository.findAllByCreator(email, paging);
        } else {
            workoutPlansPage = this.workoutPlanRepository.findAllByCreatorAndTitleLike(email, "%" + text + "%", paging);
        }

        workoutPlans = workoutPlansPage.getContent();

        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlansPage.getNumber());
        response.put("totalItems", workoutPlansPage.getTotalElements());
        response.put("totalPages", workoutPlansPage.getTotalPages());

        return response;
    }

    @Override
    public Map<String, Object> getFavoriteWorkoutPlansForUser(int page, int size, Predicate predicate) {
        Page<WorkoutPlan> workoutPlansPage;
        List<WorkoutPlan> workoutPlans;
        Map<String, Object> response = new HashMap<>();

        Pageable paging = PageRequest.of(page, size);

        workoutPlansPage = this.workoutPlanRepository.findAll(predicate, paging);
        workoutPlans = workoutPlansPage.getContent();

        response.put("workoutPlans", workoutPlans);
        response.put("currentPage", workoutPlansPage.getNumber());
        response.put("totalItems", workoutPlansPage.getTotalElements());
        response.put("totalPages", workoutPlansPage.getTotalPages());

        return response;
    }
}
