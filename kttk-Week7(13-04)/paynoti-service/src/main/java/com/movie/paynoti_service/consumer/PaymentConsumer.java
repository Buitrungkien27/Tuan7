package com.movie.paynoti_service.consumer;

import com.movie.paynoti_service.producer.PaymentProducer;
import com.movie.paynoti_service.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentConsumer {

    @Autowired
    private PaymentProducer producer;

    @Value("${payment.simulation.mode:deterministic}")
    private String simulationMode;

    @Value("${payment.success.max-seats:5}")
    private int successMaxSeats;

    @KafkaListener(topics = "BOOKING_CREATED", groupId = "payment-group")
    public void handle(Booking booking) {

        System.out.println("Received BOOKING_CREATED: " + booking.getId());

        boolean success = shouldSucceed(booking);

        if (success) {
            booking.setStatus("SUCCESS");
            producer.success(booking);
        } else {
            booking.setStatus("FAILED");
            producer.fail(booking);
        }
    }

    private boolean shouldSucceed(Booking booking) {
        if ("random".equalsIgnoreCase(simulationMode)) {
            return new Random().nextBoolean();
        }

        return booking.getSeats() <= successMaxSeats;
    }
}