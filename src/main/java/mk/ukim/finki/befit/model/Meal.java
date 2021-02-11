package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String title;

    private String email;

    @OneToOne
    private Image image;

    private LocalDateTime submissionTime;

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

    public Meal(String title, String email, Image image, List<MealType> mealTypes, DietaryType dietaryType,
                Integer preparationTime, Integer cookingTime, Integer servings, String description,
                String ingredients, String preparation) {
        this.title = title;
        this.email = email;
        this.image = image;
        this.submissionTime = LocalDateTime.now();
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
