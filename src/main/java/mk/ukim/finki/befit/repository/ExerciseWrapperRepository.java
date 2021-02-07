package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.ExerciseWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseWrapperRepository extends JpaRepository<ExerciseWrapper, Long> {
    
}
