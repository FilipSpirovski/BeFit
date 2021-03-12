package mk.ukim.finki.befit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.DietaryType;
import mk.ukim.finki.befit.model.enumeration.MealType;

import javax.persistence.*;
import java.io.Serializable;
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

    private String creator;

    @ElementCollection()
    private List<String> favoriteForUsers;

    @OneToOne(cascade = {CascadeType.ALL})
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

    private Double price = 0.0;

    @Column(length = 8000)
    private String description;

    @Column(length = 8000)
    private String ingredients;

    @Column(length = 8000)
    private String preparation;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Review> reviews;

    public Double getRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        } else {
            return reviews.stream()
                    .mapToInt(Review::getScore)
                    .average().getAsDouble();
        }
    }
}
