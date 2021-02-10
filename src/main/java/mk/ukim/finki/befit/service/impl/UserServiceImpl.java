package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.model.exception.UserNotFoundException;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.repository.UserRepository;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User register(User user) {
        return this.userRepository.save(user);
    }
}
