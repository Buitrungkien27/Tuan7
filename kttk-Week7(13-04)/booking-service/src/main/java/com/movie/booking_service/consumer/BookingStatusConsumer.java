package com.movie.booking_service.consumer;

import com.movie.booking_service.model.Booking;
import com.movie.booking_service.service.BookingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingStatusConsumer {

    private final BookingService bookingService;

    public BookingStatusConsumer(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @KafkaListener(topics = "PAYMENT_COMPLETED", groupId = "booking-status-group")
    public void onPaymentCompleted(Booking booking) {
        bookingService.updateStatus(booking.getId(), "SUCCESS")
                .ifPresent(updated -> System.out.println("Booking status updated to SUCCESS: " + updated.getId()));
    }

    @KafkaListener(topics = "BOOKING_FAILED", groupId = "booking-status-group")
    public void onBookingFailed(Booking booking) {
        bookingService.updateStatus(booking.getId(), "FAILED")
                .ifPresent(updated -> System.out.println("Booking status updated to FAILED: " + updated.getId()));
    }
}
