package com.mateuslll.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/api/message")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("It works!");
    }

    @GetMapping("/api/admin")
    public ResponseEntity<String> getAdminMessage() {
        return ResponseEntity.ok("Admin only!");
    }

    @GetMapping("/api/regular")
    public ResponseEntity<String> getRegularMessage() {
        return ResponseEntity.ok("Regular only!");
    }
}
