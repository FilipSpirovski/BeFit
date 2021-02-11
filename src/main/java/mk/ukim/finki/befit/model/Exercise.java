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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String name;

    @OneToOne
    private Image image;

    @Enumerated(value = EnumType.STRING)
    private MuscleGroup muscleGroup;

    private Boolean equipment;

    public Exercise(String name, Image image, MuscleGroup muscleGroup, Boolean equipment) {
        this.name = name;
        this.image = image;
        this.muscleGroup = muscleGroup;
        this.equipment = equipment;
    }
}
