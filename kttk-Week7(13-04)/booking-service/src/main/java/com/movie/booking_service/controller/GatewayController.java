package com.movie.booking_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final RestTemplate restTemplate;

    @Value("${user.service.base-url:http://localhost:8081}")
    private String userServiceBaseUrl;

    @Value("${movie.service.base-url:http://localhost:8082}")
    private String movieServiceBaseUrl;

    public GatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> payload) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                userServiceBaseUrl + "/users/register",
                payload,
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> payload) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                userServiceBaseUrl + "/users/login",
                payload,
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/movies")
    public ResponseEntity<String> getMovies() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                movieServiceBaseUrl + "/movies",
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PostMapping("/movies")
    public ResponseEntity<String> addMovie(@RequestBody Map<String, Object> payload) {
        ResponseEntity<String> response = restTemplate.postForEntity(
                movieServiceBaseUrl + "/movies",
                payload,
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload);
        ResponseEntity<String> response = restTemplate.exchange(
                movieServiceBaseUrl + "/movies/" + id,
                HttpMethod.PUT,
                request,
                String.class
        );
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
