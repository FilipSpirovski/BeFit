package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.Article;

import java.util.List;

public interface ArticleService {
    List<Article> findAll();

    Article findById(Long id);

    Article save(Article article);

    Article edit(Article article);

    Article delete(Long id);
}
