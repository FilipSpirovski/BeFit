package mk.ukim.finki.befit.service.impl;

import mk.ukim.finki.befit.exception.UserNotFoundException;
import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.repository.UserRepository;
import mk.ukim.finki.befit.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = this.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException(email);
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList())
        );

        return userDetails;
    }
}
