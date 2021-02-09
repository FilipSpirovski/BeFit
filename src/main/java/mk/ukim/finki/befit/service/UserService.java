package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.User;

public interface UserService {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    User register(User user);
}
