package mk.ukim.finki.befit.security;

import mk.ukim.finki.befit.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrincipalUserFactory {

    public static PrincipalUser build(User user) {
        List<GrantedAuthority> authorities = Stream
                .of(new SimpleGrantedAuthority(user.getRole().toString()))
                .collect(Collectors.toList());

        return new PrincipalUser(user.getEmail(), user.getPassword(), authorities);
    }
}
