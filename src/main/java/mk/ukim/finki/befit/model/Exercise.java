package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;
import mk.ukim.finki.befit.model.enumeration.WorkoutType;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String name;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;

    @Enumerated(value = EnumType.STRING)
    private MuscleGroup muscleGroup;

    @Enumerated(value = EnumType.STRING)
    private WorkoutType workoutType;

    private Boolean equipment;

    public Exercise(String name, Image image, MuscleGroup muscleGroup, Boolean equipment) {
        this.name = name;
        this.image = image;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }
}
