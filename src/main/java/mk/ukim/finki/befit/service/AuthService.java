package mk.ukim.finki.befit.service;

import mk.ukim.finki.befit.model.User;
import mk.ukim.finki.befit.model.dto.LoginDto;
import mk.ukim.finki.befit.model.dto.ResponseDto;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    ResponseDto register(User user);

    ResponseDto login(LoginDto loginDto);

    ResponseDto logout(HttpServletRequest request) throws ServletException;

    ResponseDto getCurrentUser(Authentication authentication);
}
