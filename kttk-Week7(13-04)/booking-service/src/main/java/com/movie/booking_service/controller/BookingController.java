package com.movie.booking_service.controller;

import com.movie.booking_service.producer.BookingEventProducer;
import com.movie.booking_service.model.Booking;
import com.movie.booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService service;

    @Autowired
    private BookingEventProducer producer;

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        if (booking.getUsername() == null || booking.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        if (booking.getMovie() == null || booking.getMovie().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie is required");
        }

        if (booking.getSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats must be greater than 0");
        }

        if (booking.getPaymentMethod() == null || booking.getPaymentMethod().isBlank()) {
            booking.setPaymentMethod("CASH");
        }

        Booking saved = service.create(booking);

        producer.sendBooking(saved);

        return saved;
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    @GetMapping("/user/{username}")
    public List<Booking> getByUsername(@PathVariable String username) {
        return service.getByUsername(username);
    }
}