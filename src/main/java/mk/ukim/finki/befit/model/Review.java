package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @Column(length = 8000)
    private String description;

    @ManyToOne
    private User submitter;

    private LocalDateTime submissionTime;

    public Review(Integer score, String description, User submitter) {
        this.score = score;
        this.description = description;
        this.submitter = submitter;
        this.submissionTime = LocalDateTime.now();
    }
}
