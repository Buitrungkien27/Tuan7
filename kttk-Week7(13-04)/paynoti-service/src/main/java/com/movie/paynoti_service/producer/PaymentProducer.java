package com.movie.paynoti_service.producer;

import com.movie.paynoti_service.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void success(Booking booking) {
        kafkaTemplate.send("PAYMENT_COMPLETED", booking);
        System.out.println("PAYMENT_COMPLETED: " + booking.getId());
    }

    public void fail(Booking booking) {
        kafkaTemplate.send("BOOKING_FAILED", booking);
        System.out.println("BOOKING_FAILED: " + booking.getId());
    }
}