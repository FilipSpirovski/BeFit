package mk.ukim.finki.befit.web;

import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/count")
    public Integer getNumberOfUsers() {
        return this.userService.getNumberOfUsers();
    }
}
