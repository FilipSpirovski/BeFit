package mk.ukim.finki.befit.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.TokenDto;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.enumeration.UserRole;
import mk.ukim.finki.befit.security.jwt.JwtProvider;
import mk.ukim.finki.befit.service.OAuth2Service;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;

@Service
public class OAuth2ServiceImpl implements OAuth2Service {

    @Value("${google.clientId}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public OAuth2ServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
                             AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public TokenDto login(User user) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), secretPsw));
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String jwt = this.jwtProvider.generateToken(authentication);

        TokenDto tokenDto = new TokenDto();
        UserDto userDto = new UserDto(user);

        tokenDto.setValue(jwt);
        tokenDto.setUser(userDto);

        return tokenDto;
    }

    @Override
    public ResponseEntity<TokenDto> oauth2WithGoogle(TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier
                .Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(this.googleClientId));

        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();

        User user = this.getGoogleUser(payload);
        TokenDto tokenResponse = this.login(user);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @Override
    public User getGoogleUser(GoogleIdToken.Payload payload) {
        if (this.userService.existsByEmail(payload.getEmail())) {
            return this.userService.findByEmail(payload.getEmail());
        } else {
            return this.saveGoogleUser(payload);
        }
    }

    @Override
    public User saveGoogleUser(GoogleIdToken.Payload payload) {
        User user = new User();

        user.setEmail(payload.getEmail());
        user.setUsername(payload.getEmail());
        user.setPassword(this.passwordEncoder.encode(this.secretPsw));
        user.setName(payload.get("given_name").toString());
        user.setSurname(payload.get("family_name").toString());
        user.setRole(UserRole.ROLE_USER);
        user.setProfilePictureUrl(payload.get("picture").toString());

        return this.userService.register(user);
    }

    @Override
    public ResponseEntity<TokenDto> oauth2WithFacebook(TokenDto tokenDto) {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "first_name", "last_name", "picture"};
        org.springframework.social.facebook.api.User facebookUser = facebook.fetchObject(
                "me", org.springframework.social.facebook.api.User.class, fields);

        User user = this.getFacebookUser(facebookUser);
        TokenDto tokenResponse = this.login(user);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @Override
    public User getFacebookUser(org.springframework.social.facebook.api.User facebookUser) {
        if (this.userService.existsByEmail(facebookUser.getEmail())) {
            return this.userService.findByEmail(facebookUser.getEmail());
        } else {
            return this.saveFacebookUser(facebookUser);
        }
    }

    @Override
    public User saveFacebookUser(org.springframework.social.facebook.api.User facebookUser) {
        User user = new User();

        user.setEmail(facebookUser.getEmail());
        user.setUsername(facebookUser.getEmail());
        user.setPassword(this.passwordEncoder.encode(this.secretPsw));
        user.setName(facebookUser.getFirstName());
        user.setSurname(facebookUser.getLastName());
        user.setRole(UserRole.ROLE_USER);
        LinkedHashMap<String, LinkedHashMap<String, Object>> picture = (LinkedHashMap)
                facebookUser.getExtraData().get("picture");
        user.setProfilePictureUrl(picture.get("data").get("url").toString());

        return this.userService.register(user);
    }
}
