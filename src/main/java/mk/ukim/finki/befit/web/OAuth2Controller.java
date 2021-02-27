package mk.ukim.finki.befit.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.opencensus.trace.Link;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.TokenDto;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.enumeration.UserRole;
import mk.ukim.finki.befit.security.jwt.JwtProvider;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/oauth")
@CrossOrigin("http://localhost:4200")
public class OAuth2Controller {

    @Value("${google.clientId}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public OAuth2Controller(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                            JwtProvider jwtProvider, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<TokenDto> google(@RequestBody TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier
                .Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(googleClientId));

        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();

        User user = getGoogleUser(payload);
        TokenDto tokenResponse = login(user);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<TokenDto> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "first_name", "last_name", "picture"};
        org.springframework.social.facebook.api.User facebookUser = facebook.fetchObject(
                "me", org.springframework.social.facebook.api.User.class, fields
        );

        User user = getFacebookUser(facebookUser);
        TokenDto tokenResponse = login(user);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    private User getGoogleUser(GoogleIdToken.Payload payload) {
        if (userService.existsByEmail(payload.getEmail())) {
            return userService.findByEmail(payload.getEmail());
        } else {
            return saveGoogleUser(payload);
        }
    }

    private User getFacebookUser(org.springframework.social.facebook.api.User facebookUser) {
        if (userService.existsByEmail(facebookUser.getEmail())) {
            return userService.findByEmail(facebookUser.getEmail());
        } else {
            return saveFacebookUser(facebookUser);
        }
    }

    private TokenDto login(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), secretPsw)
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        TokenDto tokenDto = new TokenDto();
        UserDto userDto = new UserDto(user);

        tokenDto.setValue(jwt);
        tokenDto.setUser(userDto);

        return tokenDto;
    }

    private User saveGoogleUser(GoogleIdToken.Payload payload) {
        User user = new User();

        user.setEmail(payload.getEmail());
        user.setUsername(payload.getEmail());
        user.setPassword(passwordEncoder.encode(secretPsw));
        user.setName(payload.get("given_name").toString());
        user.setSurname(payload.get("family_name").toString());
        user.setRole(UserRole.ROLE_USER);
        user.setProfilePictureUrl(payload.get("picture").toString());

        return userService.register(user);
    }

    private User saveFacebookUser(org.springframework.social.facebook.api.User facebookUser) {
        User user = new User();

        user.setEmail(facebookUser.getEmail());
        user.setUsername(facebookUser.getEmail());
        user.setPassword(passwordEncoder.encode(secretPsw));
        user.setName(facebookUser.getFirstName());
        user.setSurname(facebookUser.getLastName());
        user.setRole(UserRole.ROLE_USER);
        LinkedHashMap<String, LinkedHashMap<String, Object>> picture = (LinkedHashMap) facebookUser
                .getExtraData()
                .get("picture");
        user.setProfilePictureUrl(picture.get("data").get("url").toString());

        return userService.register(user);
    }
}
