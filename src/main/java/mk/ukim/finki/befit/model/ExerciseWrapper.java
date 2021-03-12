package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercise_wrappers")
public class ExerciseWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temporary_dto_id")
    private Long exerciseId;

    @OneToOne()
    private Exercise exercise;

    private Integer numberOfSets;

    private Integer numberOfReps;

    public ExerciseWrapper(Long exerciseId, Exercise exercise, Integer numberOfSets, Integer numberOfReps) {
        this.exerciseId = exerciseId;
        this.exercise = exercise;
        this.numberOfSets = numberOfSets;
        this.numberOfReps = numberOfReps;
    }
}
