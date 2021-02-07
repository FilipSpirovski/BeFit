package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.BodyPart;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private WorkoutType workoutType;

    private Boolean equipment;

    @Enumerated(value = EnumType.STRING)
    private BodyPart bodyPart;

    @ElementCollection()
    @Enumerated(value = EnumType.STRING)
    private List<MuscleGroup> muscleGroups;

    @OneToMany
    private List<ExerciseWrapper> exercises;

    @OneToMany
    private List<Review> reviews;

    public WorkoutPlan(String username, String description, WorkoutType workoutType, Boolean equipment, BodyPart bodyPart) {
        this.username = username;
        this.description = description;
        this.workoutType = workoutType;
        this.equipment = equipment;
        this.bodyPart = bodyPart;
        this.muscleGroups = new ArrayList<>();
        this.exercises = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }
}
