package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByTitleLike(String text);
}
