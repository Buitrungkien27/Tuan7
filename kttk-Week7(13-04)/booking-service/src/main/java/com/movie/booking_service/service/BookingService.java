package com.movie.booking_service.service;

import com.movie.booking_service.model.Booking;
import com.movie.booking_service.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking create(Booking booking) {
        booking.setStatus("PENDING");
        if (booking.getPaymentMethod() == null || booking.getPaymentMethod().isBlank()) {
            booking.setPaymentMethod("CASH");
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getByUsername(String username) {
        return bookingRepository.findByUsernameOrderByIdDesc(username);
    }

    public Optional<Booking> updateStatus(Long id, String status) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(status);
            return bookingRepository.save(booking);
        });
    }
}