package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.Comment;
import mk.ukim.finki.befit.model.Rating;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.exception.ArticleNotFoundException;
import mk.ukim.finki.befit.model.exception.CommentNotFoundException;
import mk.ukim.finki.befit.repository.ArticleRepository;
import mk.ukim.finki.befit.repository.RatingRepository;
import mk.ukim.finki.befit.service.ArticleService;
import mk.ukim.finki.befit.service.CommentService;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum")
@CrossOrigin("http://localhost:4200")
public class ForumController {
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final RatingRepository ratingRepository;
    private final UserService userService;

    public ForumController(ArticleRepository articleRepository, ArticleService articleService,
                           CommentService commentService, RatingRepository ratingRepository,
                           UserService userService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.commentService = commentService;
        this.ratingRepository = ratingRepository;
        this.userService = userService;
    }

    @GetMapping("/articles/all/{criteria}")
    public Map<String, Object> getArticles(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @PathVariable String criteria) {
        Pageable paging;
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
        Page<Article> articlePage;
        List<Article> articles;
        Map<String, Object> response = new HashMap<>();

        articlePage = this.articleRepository.findAll(paging);
        articles = articlePage.getContent();
        response.put("articles", articles);
        response.put("currentPage", articlePage.getNumber());
        response.put("totalItems", articlePage.getTotalElements());
        response.put("totalPages", articlePage.getTotalPages());

        return response;
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
    public Article addArticle(@RequestBody Article article, Authentication authentication) {
        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        article.setSubmitter(user);
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
    public Comment addCommentToArticle(@PathVariable Long id, @RequestBody Comment comment, Authentication authentication) {
        try {
            Article article = this.articleService.findById(id);
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);

            comment.setSubmitter(user);
            comment.setSubmissionTime(LocalDateTime.now());
            comment.setRating(0);
            comment = this.commentService.save(comment);
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
    public Comment changeRatingOfComment(@PathVariable Long id, @PathVariable String vote, Authentication authentication) {
        try {
            Comment comment = this.commentService.findById(id);
            String userEmail = (String) authentication.getPrincipal();
            User user = this.userService.findByEmail(userEmail);
            Rating rating = this.ratingRepository.findByEmailAndCommentId(userEmail, id);

            switch (vote) {
                case "upVote":
                    comment.upVote();
                    break;
                case "downVote":
                    comment.downVote();
                    break;
            }
            this.commentService.edit(comment);

            if (rating != null) {
                rating.setVote(vote);
                this.ratingRepository.save(rating);
            } else {
                rating = new Rating();
                rating.setEmail(userEmail);
                rating.setCommentId(comment.getId());
                rating.setVote(vote);
                rating = this.ratingRepository.save(rating);
                user.getLikedComments().add(rating);
                this.userService.edit(user);
            }

            return comment;
        } catch (CommentNotFoundException e) {
            System.out.println(e.getMessage());

            return null;
        }
    }
}
