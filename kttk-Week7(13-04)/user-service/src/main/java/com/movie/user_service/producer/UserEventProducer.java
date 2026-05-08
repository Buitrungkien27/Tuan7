package com.movie.user_service.producer;

import com.movie.user_service.model.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRegistered(String username) {
        UserRegisteredEvent event = new UserRegisteredEvent(username);
        kafkaTemplate.send("USER_REGISTERED", event);
        System.out.println("Sent USER_REGISTERED event: " + username);
    }
}