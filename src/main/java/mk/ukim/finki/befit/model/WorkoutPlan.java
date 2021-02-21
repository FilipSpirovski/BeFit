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

    private String username;

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

    public WorkoutPlan(String username, String title, String description, Image image,
                       WorkoutType workoutType, Boolean equipment, BodyPart bodyPart) {
        this.username = username;
        this.title = title;
        this.image = image;
        this.description = description;
        this.submissionTime = LocalDateTime.now();
        this.workoutType = workoutType;
        this.equipment = equipment;
        this.bodyPart = bodyPart;
        this.muscleGroups = new ArrayList<>();
        this.exercises = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public Integer getRating() {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        } else {
            return (int) reviews.stream()
                    .mapToInt(Review::getScore)
                    .average().getAsDouble();
        }
    }
}
