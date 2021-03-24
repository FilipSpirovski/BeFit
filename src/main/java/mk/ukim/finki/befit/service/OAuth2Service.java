package mk.ukim.finki.befit.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.TokenDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface OAuth2Service {

    TokenDto login(User user);

    ResponseEntity<TokenDto> oauth2WithGoogle(TokenDto tokenDto) throws IOException;

    User getGoogleUser(GoogleIdToken.Payload payload);

    User saveGoogleUser(GoogleIdToken.Payload payload);

    ResponseEntity<TokenDto> oauth2WithFacebook(TokenDto tokenDto);

    User getFacebookUser(org.springframework.social.facebook.api.User facebookUser);

    User saveFacebookUser(org.springframework.social.facebook.api.User facebookUser);
}
