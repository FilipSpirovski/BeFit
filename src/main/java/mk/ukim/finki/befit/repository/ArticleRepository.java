package mk.ukim.finki.befit.repository;

import mk.ukim.finki.befit.model.Article;
import mk.ukim.finki.befit.model.QArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {

    Page<Article> findAllByTitleLike(String text, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle article) {
        bindings.bind(article.title).first((path, value) -> path.toLowerCase().contains(value.toLowerCase()));
    }
}
