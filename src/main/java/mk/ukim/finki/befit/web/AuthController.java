package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.LoginDto;
import mk.ukim.finki.befit.model.dto.ResponseDto;
import mk.ukim.finki.befit.model.dto.UserDto;
import mk.ukim.finki.befit.model.enumeration.UserRole;
import mk.ukim.finki.befit.security.jwt.JwtProvider;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseDto register(@RequestBody User user) {
        String email = user.getEmail();
        ResponseDto response = new ResponseDto();

        if (this.userService.existsByEmail(email)) {
            response.setStatusCode(401);
            response.setMessage(String.format("The user with the provided email (%s) already exists!", email));
            response.setUser(null);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(UserRole.ROLE_USER);
            user = this.userService.register(user);

            response.setStatusCode(200);
            response.setMessage("Successful registration!");
            response.setUser(new UserDto(user));
        }

        return response;
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginDto loginDto) {
        String email = loginDto.getEmail();
        ResponseDto response = new ResponseDto();

        if (!this.userService.existsByEmail(email)) {
            response.setStatusCode(401);
            response.setMessage(String.format("The user with the provided email (%s) does not exist!", email));
            response.setUser(null);
        } else {
            User user = this.userService.findByEmail(email);
            String password = loginDto.getPassword();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                response.setStatusCode(401);
                response.setMessage("Passwords do not match!");
                response.setUser(null);
            } else {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
                String jwt = jwtProvider.generateToken(authentication);

                response.setStatusCode(200);
                response.setMessage("Successful registration!");
                response.setUser(new UserDto(user));
                response.setToken(jwt);
            }
        }

        return response;
    }

    @PostMapping("/logout")
    public ResponseDto logout(HttpServletRequest request) throws ServletException {
        ResponseDto response = new ResponseDto();

        request.logout();

        response.setStatusCode(200);
        response.setMessage("Successful logging out!");
        response.setUser(null);

        return response;
    }

    @GetMapping("/current-user")
    public UserDto getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        String userEmail = (String) authentication.getPrincipal();
        User user = this.userService.findByEmail(userEmail);

        return new UserDto(user);
    }
}
