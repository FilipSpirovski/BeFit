package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Rating;
import mk.ukim.finki.befit.model.exception.RatingNotFoundException;
import mk.ukim.finki.befit.repository.RatingRepository;
import mk.ukim.finki.befit.service.RatingService;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating findById(Long id) {
        return this.ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException(id));
    }

    @Override
    public Rating findByUserAndComment(String userEmail, Long commentId) {
        return this.ratingRepository.findByEmailAndCommentId(userEmail, commentId);
    }

    @Override
    public Rating save(Rating rating) {
        return this.ratingRepository.save(rating);
    }

    @Override
    public Rating edit(Rating rating) {
        Rating existingRating = this.findById(rating.getId());

        existingRating.setVote(rating.getVote());

        return this.ratingRepository.save(existingRating);
    }
}
