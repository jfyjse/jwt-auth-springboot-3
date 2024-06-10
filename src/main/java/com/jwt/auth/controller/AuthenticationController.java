package com.jwt.auth.controller;


import com.jwt.auth.model.AuthenticationResponse;
import com.jwt.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(
//            @RequestBody Users request
//    ) {
//        return ResponseEntity.ok(authService.register(request));
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestParam String username
            ) {
        return ResponseEntity.ok(authService.authenticate(username));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) {
        return authService.refreshToken(request);
    }
}
