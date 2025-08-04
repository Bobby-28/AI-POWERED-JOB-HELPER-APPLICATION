package com.user_consumer.service;

import com.user_consumer.entities.User;
import com.user_consumer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {
    @Autowired
    UserRepository userRepository;

    @KafkaListener(topics = "user-data-topic-ecommerce", groupId = "user-consumer-group")
    public void listen(User user){
        try {
            userRepository.save(user);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
