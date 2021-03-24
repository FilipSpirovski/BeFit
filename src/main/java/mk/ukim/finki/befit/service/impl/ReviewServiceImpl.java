package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Review;
import mk.ukim.finki.befit.model.exception.ReviewNotFoundException;
import mk.ukim.finki.befit.repository.ReviewRepository;
import mk.ukim.finki.befit.service.ReviewService;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review findById(Long id) {
        return this.reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    @Override
    public Review save(Review review) {
        return this.reviewRepository.save(review);
    }

    @Override
    public Review edit(Review review) {
        Review existingReview = this.findById(review.getId());

        existingReview.setScore(review.getScore());
        existingReview.setDescription(review.getDescription());

        return this.reviewRepository.save(existingReview);
    }

    @Override
    public Review delete(Long id) {
        Review review = this.findById(id);

        this.reviewRepository.delete(review);

        return review;
    }
}
