package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Review;

public interface ReviewService {

    Review findById(Long id);

    Review save(Review review);

    Review edit(Review review);

    Review delete(Long id);
}
