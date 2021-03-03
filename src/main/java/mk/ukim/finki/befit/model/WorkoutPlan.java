package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.BodyPart;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String creator;

    @ElementCollection()
    private List<String> favoriteForUsers;

    @Column(length = 1000)
    private String title;

    @Column(length = 8000)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private Image image;

    private LocalDateTime submissionTime;

    @Enumerated(value = EnumType.STRING)
    private WorkoutType workoutType;

    private Boolean equipment;

    @Enumerated(value = EnumType.STRING)
    private BodyPart bodyPart;

    @ElementCollection()
    @Enumerated(value = EnumType.STRING)
    private List<MuscleGroup> muscleGroups;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ExerciseWrapper> exercises;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Review> reviews;

    private Double price = 0.0;

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
