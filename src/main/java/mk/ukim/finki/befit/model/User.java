package mk.ukim.finki.befit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "befit_users")
public class User implements UserDetails {

    @Id
    private String email;

    private String username;

    private String password;

    private String name;

    private String surname;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @ManyToMany
    private List<WorkoutPlan> favoriteWorkoutPlans;

    @ManyToMany
    private List<Meal> favoriteMeals;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
