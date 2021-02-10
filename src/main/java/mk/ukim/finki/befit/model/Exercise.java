package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.befit.model.enumeration.MuscleGroup;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private MuscleGroup muscleGroup;

    public Exercise(String name, String description, MuscleGroup muscleGroup) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
    }
}
