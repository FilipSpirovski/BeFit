package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Comment;

public interface CommentService {

    Comment findById(Long id);

    Comment save(Comment comment);

    Comment edit(Comment comment);

    Comment delete(Long id);
}
