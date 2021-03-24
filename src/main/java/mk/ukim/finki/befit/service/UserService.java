package mk.ukim.finki.befit.service;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.User;

import java.util.Map;

public interface UserService {

    Integer getNumberOfUsers();

    boolean existsByEmail(String email);

    User findByEmail(String email);

    User register(User user);

    User edit(User user);

    Map<String, Object> getMealsCreatedByUser(String userEmail, int page, int size, String text);

    Map<String, Object> getFavoriteMealsForUser(int page, int size, Predicate predicate);

    Map<String, Object> getWorkoutPlansCreatedByUser(int page, int size, String email, String text);

    Map<String, Object> getFavoriteWorkoutPlansForUser(int page, int size, Predicate predicate);
}
