package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.dto.TokenDto;
import mk.ukim.finki.befit.service.OAuth2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@CrossOrigin("http://localhost:4200")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    public OAuth2Controller(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    @PostMapping("/google")
    public ResponseEntity<TokenDto> google(@RequestBody TokenDto tokenDto) throws IOException {
        return this.oAuth2Service.oauth2WithGoogle(tokenDto);
    }

    @PostMapping("/facebook")
    public ResponseEntity<TokenDto> facebook(@RequestBody TokenDto tokenDto) {
        return this.oAuth2Service.oauth2WithFacebook(tokenDto);
    }
}
