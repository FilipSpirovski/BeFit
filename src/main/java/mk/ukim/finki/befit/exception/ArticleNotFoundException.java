package mk.ukim.finki.befit.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(Long id) {
        super(String.format("The article with the provided id (%d) does not exist!", id));
    }
}
