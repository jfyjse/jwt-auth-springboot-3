package com.jwt.auth.service;


import com.jwt.auth.model.AuthenticationResponse;
import com.jwt.auth.model.Token;
import com.jwt.auth.model.Users;
import com.jwt.auth.repository.TokenRepository;
import com.jwt.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(Users request) {

        // check if user already exist. if exist than authenticate the user
        if(repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "User already exist");
        }

        Users users = new Users();
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setUsername(request.getUsername());
        users.setPassword(passwordEncoder.encode(request.getPassword()));


        users.setRole(request.getRole());

        users = repository.save(users);

        String jwt = jwtService.generateToken(users);

        saveUserToken(jwt, users);

        return new AuthenticationResponse(jwt, "User registration was successful");

    }

    public AuthenticationResponse authenticate(Users request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Users users = repository.findByUsername(request.getUsername()).orElseThrow();
        String jwt = jwtService.generateToken(users);

        revokeAllTokenByUser(users);
        saveUserToken(jwt, users);

        return new AuthenticationResponse(jwt, "User login was successful");

    }
    private void revokeAllTokenByUser(Users users) {
        List<Token> validTokens = tokenRepository.findAllTokensByUser(users.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String jwt, Users users) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUsers(users);
        tokenRepository.save(token);
    }
}
