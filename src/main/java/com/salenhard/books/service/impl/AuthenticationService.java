package com.salenhard.books.service.impl;

import com.salenhard.books.entity.DTO.SignInRequest;
import com.salenhard.books.entity.User;
import com.salenhard.books.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;


    public User signUp(User user) {
        log.info("Creating user - Username:{}", user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user = userService.save(user);
        log.info("User created successfully - ID: {}", user.getId());
        return user;

    }

    public String signIn(SignInRequest signInRequest) {
        log.info("Authenticating user - Username:{}", signInRequest.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getUsername(),
                signInRequest.getPassword()
        ));
        UserDetails user = userService.loadUserByUsername(signInRequest.getUsername());
        log.info("User successfully sign in {}", signInRequest.getUsername());
        return jwtService.generateToken(user);
    }
}
