package mk.ukim.finki.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.UserRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "befit_users")
public class User {

    @Id
    private String email;

    private String username;

    private String password;

    private String name;

    private String surname;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Column(length = 500)
    private String profilePictureUrl;

    @ManyToMany
    private List<WorkoutPlan> favoriteWorkoutPlans;

    @ManyToMany
    private List<Meal> favoriteMeals;

    @OneToMany
    private List<Rating> likedComments;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
