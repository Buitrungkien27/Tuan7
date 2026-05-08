package com.movie.movie_service.service;

import com.movie.movie_service.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class MovieService {

    private final List<Movie> movies = new ArrayList<>();

    // 🔥 giúp demo luôn có data
    @PostConstruct
    public void init() {
        movies.add(new Movie(1L, "Avengers"));
        movies.add(new Movie(2L, "Batman"));
    }

    public List<Movie> getAll() {
        return movies;
    }

    public Movie add(Movie movie) {
        movie.setId(System.currentTimeMillis());
        movies.add(movie);
        return movie;
    }

    public Movie update(Long id, Movie movie) {
        Movie existing = movies.stream()
                .filter(m -> Objects.equals(m.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));

        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie title is required");
        }

        existing.setTitle(movie.getTitle());
        return existing;
    }
}