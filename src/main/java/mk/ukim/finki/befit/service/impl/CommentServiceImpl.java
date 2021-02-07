package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.model.exception.CommentNotFoundException;
import mk.ukim.finki.befit.repository.CommentRepository;
import mk.ukim.finki.befit.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
        return null;
    }

    @Override
    public Comment delete(Long id) {
        Comment comment = this.findById(id);

        this.commentRepository.delete(comment);

        return comment;
    }
}
