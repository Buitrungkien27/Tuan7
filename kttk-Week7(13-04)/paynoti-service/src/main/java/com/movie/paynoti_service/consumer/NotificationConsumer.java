package com.movie.paynoti_service.consumer;

import com.movie.paynoti_service.model.Booking;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(topics = "PAYMENT_COMPLETED", groupId = "notify-group")
    public void success(Booking booking) {
        System.out.println("✅ Booking #" + booking.getId() + " thành công!");
    }

    @KafkaListener(topics = "BOOKING_FAILED", groupId = "notify-group")
    public void fail(Booking booking) {
        System.out.println("❌ Booking #" + booking.getId() + " thất bại!");
    }
}