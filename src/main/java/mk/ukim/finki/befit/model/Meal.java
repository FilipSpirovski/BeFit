package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String username;

    @ElementCollection()
    @Enumerated(value = EnumType.STRING)
    private List<MealType> mealTypes;

    @Enumerated(value = EnumType.STRING)
    private DietaryType dietaryType;

    private Integer preparationTime;

    private Integer cookingTime;

    private Integer servings;

    @Column(length = 8000)
    private String description;

    @Column(length = 8000)
    private String ingredients;

    @Column(length = 8000)
    private String preparation;

    @OneToMany
    private List<Review> reviews;

    public Meal(String title, String username, List<MealType> mealTypes, DietaryType dietaryType, Integer preparationTime,
                Integer cookingTime, Integer servings, String description, String ingredients, String preparation) {
        this.title = title;
        this.username = username;
        this.mealTypes = mealTypes;
        this.dietaryType = dietaryType;
        this.preparationTime = preparationTime;
        this.cookingTime = cookingTime;
        this.servings = servings;
        this.description = description;
        this.ingredients = ingredients;
        this.preparation = preparation;
        this.reviews = new ArrayList<>();
    }
}
