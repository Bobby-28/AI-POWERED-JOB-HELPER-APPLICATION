package user_service.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import user_service.model.User;

import java.util.Map;

public class UserSerializer implements Serializer<User> {
    ObjectMapper objectMapper= new ObjectMapper();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, User user) {
        byte[] val= null;
        try{
            val= objectMapper.writeValueAsString(user).getBytes();
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
