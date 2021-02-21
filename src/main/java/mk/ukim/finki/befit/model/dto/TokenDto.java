package mk.ukim.finki.befit.model.dto;

import lombok.Data;
import mk.ukim.finki.befit.model.User;

@Data
public class TokenDto {

    private String value;

    private User user;
}
