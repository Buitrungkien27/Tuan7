package com.movie.movie_service.controller;

import com.movie.movie_service.model.Movie;
import com.movie.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // 🔥 CORS fix luôn
@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    public List<Movie> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Movie add(@RequestBody Movie movie) {
        return service.add(movie);
    }

    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        return service.update(id, movie);
    }
}