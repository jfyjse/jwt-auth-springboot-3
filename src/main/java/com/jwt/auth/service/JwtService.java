package com.jwt.auth.service;


import com.jwt.auth.model.Token;
import com.jwt.auth.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public boolean isValid(String token, String user) {
        String username = extractUsername(token);

        boolean validToken = tokenRepository.findByAccessToken(token)
                .map(Token::isLoggedOut)
                .orElse(false);

        return (username.equals(user)) && isTokenExpired(token) && validToken;
    }

    public boolean isValidRefreshToken(String token, String users) {
        String username = extractUsername(token);
        boolean validRefreshToken = tokenRepository
                .findByRefreshToken(token)
                .map(Token::isLoggedOut)
                .orElse(false);

        return (username.equals(users)) && isTokenExpired(token) && validRefreshToken;
    }

    public boolean isWSValidToken(String extractedToken, String username) {
        boolean validAccessToken = tokenRepository
                .findByAccessToken(extractedToken)
                .map(Token::isLoggedOut)
                .orElse(false);
        return (isTokenExpired(extractedToken)&&validAccessToken);
    }


    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateAccessToken(String users) {
        return generateToken(users, accessTokenExpiration);
    }

    public String generateRefreshToken(String users) {

        return generateToken(users, refreshTokenExpiration);

    }

    private String generateToken(String users, long expiryTime) {

        return Jwts
                .builder()
                .subject(users)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}

