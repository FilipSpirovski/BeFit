package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long>, QuerydslPredicateExecutor<WorkoutPlan> {

}
