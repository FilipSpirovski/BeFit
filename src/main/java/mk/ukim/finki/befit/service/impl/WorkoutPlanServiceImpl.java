package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.WorkoutPlan;
import mk.ukim.finki.befit.model.exception.WorkoutPlanNotFoundException;
import mk.ukim.finki.befit.repository.ReviewRepository;
import mk.ukim.finki.befit.repository.WorkoutPlanRepository;
import mk.ukim.finki.befit.service.WorkoutPlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ReviewRepository reviewRepository;

    public WorkoutPlanServiceImpl(WorkoutPlanRepository workoutPlanRepository,
                                  ReviewRepository reviewRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<WorkoutPlan> findAll() {
        return this.workoutPlanRepository.findAll();
    }

    @Override
    public WorkoutPlan findById(Long id) {
        return this.workoutPlanRepository.findById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(id));
    }

    @Override
    public WorkoutPlan save(WorkoutPlan workoutPlan) {
        return this.workoutPlanRepository.save(workoutPlan);
    }

    @Override
    public WorkoutPlan edit(WorkoutPlan workoutPlan, boolean newImage) {
        WorkoutPlan existingWorkoutPlan = this.findById(workoutPlan.getId());

        existingWorkoutPlan.setTitle(workoutPlan.getTitle());
        existingWorkoutPlan.setDescription(workoutPlan.getDescription());
        if (newImage) {
            existingWorkoutPlan.setImage(workoutPlan.getImage());
        }
        existingWorkoutPlan.setWorkoutType(workoutPlan.getWorkoutType());
        existingWorkoutPlan.setEquipment((workoutPlan.getEquipment()));
        existingWorkoutPlan.setBodyPart(workoutPlan.getBodyPart());
        existingWorkoutPlan.setMuscleGroups(workoutPlan.getMuscleGroups());
        existingWorkoutPlan.setExercises(workoutPlan.getExercises());

        return this.workoutPlanRepository.save(existingWorkoutPlan);
    }

    @Override
    public WorkoutPlan delete(Long id) {
        WorkoutPlan workoutPlan = this.findById(id);

        workoutPlan.getReviews().forEach(this.reviewRepository::delete);
        this.workoutPlanRepository.delete(workoutPlan);

        return workoutPlan;
    }
}
