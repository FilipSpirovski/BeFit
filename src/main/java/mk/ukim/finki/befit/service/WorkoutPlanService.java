package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.WorkoutPlan;

import java.util.List;

public interface WorkoutPlanService {
    List<WorkoutPlan> findAll();

    WorkoutPlan findById(Long id);

    WorkoutPlan save(WorkoutPlan workoutPlan);

    WorkoutPlan edit(WorkoutPlan workoutPlan);

    WorkoutPlan delete(Long id);
}
