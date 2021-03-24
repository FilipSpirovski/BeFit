package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Rating;

public interface RatingService {

    Rating findById(Long id);

    Rating findByUserAndComment(String userEmail, Long commentId);

    Rating save(Rating rating);

    Rating edit(Rating rating);
}
