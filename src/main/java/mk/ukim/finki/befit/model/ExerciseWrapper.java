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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Exercise exercise;

    private Integer numberOfSets;

    @ElementCollection()
    private List<Integer> repsPerSet;

    public ExerciseWrapper(Exercise exercise, Integer numberOfSets, List<Integer> repsPerSet) {
        this.exercise = exercise;
        this.numberOfSets = numberOfSets;
        this.repsPerSet = repsPerSet;
    }
}
