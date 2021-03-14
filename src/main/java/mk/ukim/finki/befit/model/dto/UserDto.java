package mk.ukim.finki.befit.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.Rating;
import mk.ukim.finki.befit.model.Meal;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.enumeration.UserRole;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDto {

    private String email;

    private String username;

    private String password;

    private String name;

    private String surname;

    private UserRole role;

    private String profilePictureUrl;

    private List<Long> favoriteWorkoutPlans;

    private List<Long> favoriteMeals;

    private List<Rating> likedComments;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.likedComments = user.getLikedComments();

        if (user.getFavoriteWorkoutPlans() != null) {
            this.favoriteWorkoutPlans = user.getFavoriteWorkoutPlans()
                    .stream()
                    .map(WorkoutPlan::getId)
                    .collect(Collectors.toList());
        }

        if (user.getFavoriteMeals() != null) {
            this.favoriteMeals = user.getFavoriteMeals()
                    .stream()
                    .map(Meal::getId)
                    .collect(Collectors.toList());
        }
    }
}
