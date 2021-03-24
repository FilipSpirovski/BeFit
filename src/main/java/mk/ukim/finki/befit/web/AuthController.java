package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.LoginDto;
import mk.ukim.finki.befit.model.dto.ResponseDto;
import mk.ukim.finki.befit.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseDto register(@RequestBody User user) {
        return this.authService.register(user);
    }

    @PostMapping("/login")
    public Object login(@RequestBody LoginDto loginDto) {
        return this.authService.login(loginDto);
    }

    @PostMapping("/logout")
    public ResponseDto logout(HttpServletRequest request) throws ServletException {
        return this.authService.logout(request);
    }

    @GetMapping("/current-user")
    public ResponseDto getCurrentUser(Authentication authentication) {
        return this.authService.getCurrentUser(authentication);
    }
}
