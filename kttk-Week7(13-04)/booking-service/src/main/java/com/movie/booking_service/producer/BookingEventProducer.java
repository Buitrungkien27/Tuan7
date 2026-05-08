package com.movie.booking_service.producer;

import com.movie.booking_service.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBooking(Booking booking) {
        kafkaTemplate.send("BOOKING_CREATED", booking);
        System.out.println("🔥 BOOKING_CREATED: " + booking.getId());
    }
}