package com.jwt.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public ResponseEntity<String> userAndAdmin() {
        return ResponseEntity.ok("Hello from user/admin secured url");
    }

    @GetMapping("/admin_only")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Hello from admin secured url");
    }
}