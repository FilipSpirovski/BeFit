package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String title;

    @Column(length = 8000)
    private String description;

    @ManyToOne
    private User submitter;

    private LocalDateTime submissionTime;

    private Integer views;

    @OneToMany
    private List<Comment> comments;

    public Article(String title, String description, User submitter) {
        this.title = title;
        this.description = description;
        this.submitter = submitter;
        this.submissionTime = LocalDateTime.now();
    }

    public void click() {
        views += 1;
    }
}
