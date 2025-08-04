package com.user_consumer.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_consumer.entities.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class UserDeserializer implements Deserializer<User> {
    ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public User deserialize(String s, byte[] bytes) {
        User user= null;
        try {
            user= objectMapper.readValue(bytes, User.class);
        }catch (Exception e){
            throw new RuntimeException("❌ Failed to deserialize message", e);
        }
        return user;
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
