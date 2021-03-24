package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Comment;
import org.springframework.security.core.Authentication;

public interface CommentService {

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment edit(Comment comment);

    Comment changeRating(Long id, String vote, Authentication authentication);

    Comment delete(Long id);
}
