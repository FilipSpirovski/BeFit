package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.exception.ArticleNotFoundException;
import mk.ukim.finki.befit.repository.ArticleRepository;
import mk.ukim.finki.befit.repository.CommentRepository;
import mk.ukim.finki.befit.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Article> findAll() {
        return this.articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return this.articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @Override
    public Article save(Article article) {
        return this.articleRepository.save(article);
    }

    @Override
    public Article edit(Article article) {
        Article existingArticle = this.findById(article.getId());

        existingArticle.setTitle(article.getTitle());
        existingArticle.setDescription(article.getDescription());

        return existingArticle;
    }

    @Override
    public Article delete(Long id) {
        Article article = this.findById(id);

        article.getComments().forEach(this.commentRepository::delete);
        this.articleRepository.delete(article);

        return article;
    }
}
