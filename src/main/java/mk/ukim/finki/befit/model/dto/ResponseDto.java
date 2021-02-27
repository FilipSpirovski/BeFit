package mk.ukim.finki.befit.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {

    private Integer statusCode;

    private String message;

    private UserDto user;

    private String token;
}
