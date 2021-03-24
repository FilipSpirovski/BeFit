package mk.ukim.finki.befit.service.impl;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.exception.ArticleNotFoundException;
import mk.ukim.finki.befit.repository.ArticleRepository;
import mk.ukim.finki.befit.service.ArticleService;
import mk.ukim.finki.befit.service.CommentService;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final CommentService commentService;

    public ArticleServiceImpl(ArticleRepository articleRepository, UserService userService,
                              CommentService commentService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Override
    public List<Article> findAll() {
        return this.articleRepository.findAll();
    }

    @Override
    public Map<String, Object> findAllByPredicate(int page, int size, String criteria, Predicate predicate) {
        Pageable paging;
        Page<Article> articlesPage;
        List<Article> articles;
        Map<String, Object> response = new HashMap<>();

        switch (criteria) {
            case "Latest":
                paging = PageRequest.of(page, size, Sort.by("submissionTime").descending());
                break;
            case "Most popular":
                paging = PageRequest.of(page, size, Sort.by("views").descending());
                break;
            default:
                paging = PageRequest.of(page, size);
        }

        articlesPage = this.articleRepository.findAll(predicate, paging);
        articles = articlesPage.getContent();

        response.put("articles", articles);
        response.put("currentPage", articlesPage.getNumber());
        response.put("totalItems", articlesPage.getTotalElements());
        response.put("totalPages", articlesPage.getTotalPages());

        return response;
    }

    @Override
    public Article findById(Long id) {
        return this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Override
    public Article save(Article article, Authentication authentication) {
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        article.setSubmitter(user);

        return this.articleRepository.save(article);
    }

    @Override
    public Article edit(Article article) {
        Article existingArticle = this.findById(article.getId());

        existingArticle.setTitle(article.getTitle());
        existingArticle.setDescription(article.getDescription());
        existingArticle.setComments(article.getComments());

        return this.articleRepository.save(existingArticle);
    }

    @Override
    public Comment addComment(Long id, Comment comment, Authentication authentication) {
        Article article = this.findById(id);
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        comment.setSubmitter(user);
        comment.setSubmissionTime(LocalDateTime.now());
        comment = this.commentService.save(comment);

        article.getComments().add(comment);
        this.edit(article);

        return comment;
    }

    @Override
    public Article removeComment(Long articleId, Long commentId) {
        Article article = this.findById(articleId);
        Comment comment = this.commentService.findById(commentId);

        article.getComments().remove(comment);

        return this.edit(article);
    }

    @Override
    public void incrementViews(Long id) {
        Article article = this.findById(id);

        article.click();
        this.edit(article);
    }

    @Override
    public Article delete(Long id) {
        Article article = this.findById(id);

        article.getComments().forEach(comment -> this.commentService.delete(comment.getId()));
        this.articleRepository.delete(article);

        return article;
    }
}
