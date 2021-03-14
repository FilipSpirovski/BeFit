package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.User;

import java.util.List;

public interface UserService {

    Integer getNumberOfUsers();

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User register(User user);

    User edit(User user);
}
