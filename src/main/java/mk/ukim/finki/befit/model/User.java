package mk.ukim.finki.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "befit_users")
public class User {

    @Id
    private String username;

    private String name;

    private String surname;

    private String email;

    private String password;

    @ManyToMany
    private List<WorkoutPlan> favoriteWorkoutPlans;

    @ManyToMany
    private List<Meal> favoriteMeals;
}
