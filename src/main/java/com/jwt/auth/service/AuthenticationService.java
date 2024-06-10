package com.jwt.auth.service;


import com.jwt.auth.model.AuthenticationResponse;
import com.jwt.auth.model.Token;
import com.jwt.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }


//    public AuthenticationResponse register(Users request) {
//
//        // check if user already exist. if exist than authenticate the user
//        if(repository.findByUsername(request.getUsername()).isPresent()) {
//            return new AuthenticationResponse(null, "User already exist");
//        }
//
//        Users users = new Users();
//        users.setFirstName(request.getFirstName());
//        users.setLastName(request.getLastName());
//        users.setUsername(request.getUsername());
//        users.setPassword(passwordEncoder.encode(request.getPassword()));
//
//
//        users.setRole(request.getRole());
//
//        users = repository.save(users);
//
//        String accessToken = jwtService.generateAccessToken(users);
//
//        String refreshToken = jwtService.generateRefreshToken(users);
//
//        saveUserToken(accessToken, refreshToken, users);
//
//        return new AuthenticationResponse(accessToken, refreshToken);
//
//    }

    public AuthenticationResponse authenticate(String username) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );

      //  Users users = repository.findByUsername(request.getUsername()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        System.out.println(accessToken);
        System.out.println(refreshToken);

       // revokeAllTokenByUser(username);
        saveUserToken(accessToken, refreshToken, username);

        return new AuthenticationResponse(accessToken, refreshToken);

    }
    private void revokeAllTokenByUser(String users) {
        System.out.println("revoke need to update !!");
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(1);
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> t.setLoggedOut(true));

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String accessToken, String refreshToken, String users) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUserId(users);
        tokenRepository.save(token);
    }

    public ResponseEntity <AuthenticationResponse>refreshToken(HttpServletRequest request) {
        //extract token from request
       String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

       if (authHeader==null || !authHeader.startsWith("Bearer ")){
           return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
       }

       String token = authHeader.substring(7);
       String username = jwtService.extractUsername(token);

       //Users users = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
       if (jwtService.isValidRefreshToken(token, username)){

           String accessToken = jwtService.generateAccessToken(username);
           String refreshToken = jwtService.generateRefreshToken(username);

           revokeAllTokenByUser(username);
           saveUserToken(accessToken, refreshToken, username);

         return new ResponseEntity<>(new AuthenticationResponse(accessToken,refreshToken), HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
