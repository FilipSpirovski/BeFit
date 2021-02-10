package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercise_wrappers")
public class ExerciseWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Exercise exercise;

    private Integer numberOfSets;

    private Integer numberOfReps;

    public ExerciseWrapper(Exercise exercise, Integer numberOfSets, Integer numberOfReps) {
        this.exercise = exercise;
        this.numberOfSets = numberOfSets;
        this.numberOfReps = numberOfReps;
    }
}
