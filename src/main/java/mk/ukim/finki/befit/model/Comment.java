package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8000)
    private String text;

    @ManyToOne
    private User submitter;

    private LocalDateTime submissionTime;

    private Integer rating = 0;

    public Comment(String text, User submitter, Integer rating) {
        this.text = text;
        this.submitter = submitter;
        this.submissionTime = LocalDateTime.now();
        this.rating = rating;
    }

    public void upVote() {
        rating += 1;
    }

    public void downVote() {
        rating -= 1;
    }
}
