package mk.ukim.finki.befit.web;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import mk.ukim.finki.befit.dto.TokenDto;
import mk.ukim.finki.befit.enumeration.UserRole;
import mk.ukim.finki.befit.model.User;
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

@RestController
@RequestMapping("/oauth")
@CrossOrigin("http://localhost:4200")
public class OauthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Value("${google.clientId}")
    String googleClientId;

    @Value("${secretPsw}")
    String secretPsw;

    public OauthController(UserService userService, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
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

        User user = findUser(payload.getEmail());
        TokenDto tokenResponse = login(user);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<TokenDto> facebook(@RequestBody TokenDto tokenDto) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
        final String[] fields = {"email", "picture"};
        org.springframework.social.facebook.api.User facebookUser = facebook.fetchObject(
                "me", org.springframework.social.facebook.api.User.class, fields
        );

        User user = findUser(facebookUser.getEmail());
        TokenDto tokenRes = login(user);

        return new ResponseEntity<>(tokenRes, HttpStatus.OK);
    }

    private User findUser(String email) {
        User user;

        if (userService.existsByEmail(email)) {
            user = userService.findByEmail(email);
        } else {
            user = registerUser(email);
        }

        return user;
    }

    private TokenDto login(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);

        return tokenDto;
    }

    private User registerUser(String email) {
        User user = new User(email, passwordEncoder.encode(secretPsw));
        user.setRole(UserRole.ROLE_USER);

        return userService.register(user);
    }
}
