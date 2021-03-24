package mk.ukim.finki.befit.service;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.Comment;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    List<Article> findAll();

    Map<String, Object> findAllByPredicate(int page, int size, String criteria, Predicate predicate);

    Article findById(Long id);

    Article save(Article article, Authentication authentication);

    Article edit(Article article);

    Comment addComment(Long id, Comment comment, Authentication authentication);

    Article removeComment(Long articleId, Long commentId);

    void incrementViews(Long id);

    Article delete(Long id);
}
