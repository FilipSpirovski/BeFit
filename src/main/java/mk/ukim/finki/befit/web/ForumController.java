package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.model.exception.ArticleNotFoundException;
import mk.ukim.finki.befit.model.exception.CommentNotFoundException;
import mk.ukim.finki.befit.service.ArticleService;
import mk.ukim.finki.befit.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/articles/all")
    public List<Article> getArticles() {
        return this.articleService.findAll();
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable Long id) {
        try {
            Article article = this.articleService.findById(id);

            article.click();
            this.articleService.edit(article);

            return article;
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/increment-views/{id}")
    public void incrementViewsForArticle(@PathVariable Long id, @RequestBody Object object) {
        Article article = this.articleService.findById(id);

        article.click();
        this.articleService.edit(article);
    }

    @PostMapping("/articles/add")
    public Article addArticle(@RequestBody Article article) {
        article.setViews(0);
        return this.articleService.save(article);
    }

    @PostMapping("/articles/edit")
    public Article editArticle(@RequestBody Article article) {
        try {
            return this.articleService.edit(article);
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/articles/{id}/delete")
    public Article deleteArticle(@PathVariable Long id) {
        try {
            return this.articleService.delete(id);
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/articles/{id}/add-comment")
    public Comment addCommentToArticle(@PathVariable Long id, @RequestBody Comment comment) {
        try {
            Article article = this.articleService.findById(id);

            comment.setSubmissionTime(LocalDateTime.now());
            comment.setRating(0);
            article.getComments().add(comment);
            this.articleService.edit(article);

            return comment;
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/comments/edit")
    public Comment editComment(@RequestBody Comment comment) {
        try {
            return this.commentService.edit(comment);
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/comments/{id}/delete")
    public Comment deleteComment(@PathVariable Long id) {
        try {
            return this.commentService.delete(id);
        } catch (ArticleNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @PostMapping("/comments/{id}/change-rating/{vote}")
    public Comment changeRatingOfComment(@PathVariable Long id, @PathVariable String vote) {
        try {
            Comment comment = this.commentService.findById(id);

            switch (vote) {
                case "upVote":
                    comment.upVote();
                    break;
                case "downVote":
                    comment.downVote();
                    break;
            }
            this.commentService.edit(comment);

            return comment;
        } catch (CommentNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
