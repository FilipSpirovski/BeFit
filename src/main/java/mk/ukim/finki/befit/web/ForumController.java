package mk.ukim.finki.befit.web;

import com.querydsl.core.types.Predicate;
import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.service.ArticleService;
import mk.ukim.finki.befit.service.CommentService;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forum")
@CrossOrigin("http://localhost:4200")
public class ForumController {

    private final ArticleService articleService;
    private final CommentService commentService;

    public ForumController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles/all/{criteria}")
    public Map<String, Object> getArticles(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @PathVariable String criteria,
                                           @QuerydslPredicate(root = Article.class) Predicate predicate) {
        return this.articleService.findAllByPredicate(page, size, criteria, predicate);
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable Long id) {
        return this.articleService.findById(id);
    }

    @PostMapping("/increment-views/{id}")
    public void incrementViewsForArticle(@PathVariable Long id,
                                         @RequestBody(required = false) Object body) {
        this.articleService.incrementViews(id);
    }

    @PostMapping("/articles/add")
    public Article addArticle(@RequestBody Article article, Authentication authentication) {
        return this.articleService.save(article, authentication);
    }

    @PostMapping("/articles/edit")
    public Article editArticle(@RequestBody Article article) {
        return this.articleService.edit(article);
    }

    @PostMapping("/articles/{id}/delete")
    public Article deleteArticle(@PathVariable Long id) {
        return this.articleService.delete(id);
    }

    @PostMapping("/articles/{id}/add-comment")
    public Comment addCommentToArticle(@PathVariable Long id,
                                       @RequestBody Comment comment,
                                       Authentication authentication) {
        return this.articleService.addComment(id, comment, authentication);
    }

    @PostMapping("/comments/edit")
    public Comment editComment(@RequestBody Comment comment) {
        return this.commentService.edit(comment);
    }

    @PostMapping("{articleId}/comments/{commentId}/delete")
    public Article deleteComment(@PathVariable Long articleId,
                                 @PathVariable Long commentId) {
        return this.articleService.removeComment(articleId, commentId);
    }

    @PostMapping("/comments/{id}/change-rating/{vote}")
    public Comment changeRatingOfComment(@PathVariable Long id,
                                         @PathVariable String vote,
                                         Authentication authentication) {
        return this.commentService.changeRating(id, vote, authentication);
    }
}
