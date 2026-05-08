package com.movie.user_service.controller;

import com.movie.user_service.dto.AuthRequest;
import com.movie.user_service.model.User;
import com.movie.user_service.producer.UserEventProducer;
import com.movie.user_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserEventProducer producer;
    private final AuthService authService;

    public UserController(UserEventProducer producer, AuthService authService) {
        this.producer = producer;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody AuthRequest request) {
        User createdUser = authService.register(request);
        producer.sendUserRegistered(createdUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered",
                "username", createdUser.getUsername()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody AuthRequest request) {
        User user = authService.login(request);

        return ResponseEntity.ok(Map.of(
                "message", "Login success",
                "username", user.getUsername()
        ));
    }
}
