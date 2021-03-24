package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.model.Rating;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.exception.CommentNotFoundException;
import mk.ukim.finki.befit.repository.CommentRepository;
import mk.ukim.finki.befit.service.CommentService;
import mk.ukim.finki.befit.service.RatingService;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final RatingService ratingService;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService,
                              RatingService ratingService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @Override
    public Comment findById(Long id) {
        return this.commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Override
    public Comment save(Comment comment) {
        return this.commentRepository.save(comment);
    }

    @Override
    public Comment edit(Comment comment) {
        Comment existingComment = this.findById(comment.getId());

        existingComment.setText(comment.getText());

        return this.commentRepository.save(existingComment);
    }

    @Override
    public Comment changeRating(Long id, String vote, Authentication authentication) {
        Comment comment = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);
        Rating rating = this.ratingService.findByUserAndComment(userEmail, id);

        switch (vote) {
            case "upVote":
                comment.upVote();
                break;
            case "downVote":
                comment.downVote();
                break;
        }
        this.edit(comment);

        if (rating != null) {
            rating.setVote(vote);
            this.ratingService.edit(rating);
        } else {
            rating = new Rating();

            rating.setEmail(userEmail);
            rating.setCommentId(comment.getId());
            rating.setVote(vote);
            rating = this.ratingService.save(rating);

            user.getLikedComments().add(rating);
            this.userService.edit(user);
        }

        return comment;
    }

    @Override
    public Comment delete(Long id) {
        Comment comment = this.findById(id);

        this.commentRepository.delete(comment);

        return comment;
    }
}
