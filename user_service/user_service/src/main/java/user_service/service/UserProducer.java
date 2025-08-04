package user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import user_service.model.User;

@Service
public class UserProducer {
    KafkaTemplate<String, User> kafkaTemplate;
    @Autowired
    UserProducer(KafkaTemplate<String, User> kafkaTemplate){
        this.kafkaTemplate= kafkaTemplate;
    }

    void senUser(User user){
        Message<User> message= MessageBuilder.withPayload(user)
                .setHeader(KafkaHeaders.TOPIC, "user-data-application").build();
        kafkaTemplate.send(message);
    }
}
