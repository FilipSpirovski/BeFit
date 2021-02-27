package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating findByEmailAndCommentId(String email, Long commentId);
}
