package user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import user_service.model.Token;
import user_service.model.User;
import user_service.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProducer userProducer;

    @Autowired
    RestTemplate restTemplate;
    public User create(User user){
        if(user==null){
            throw new RuntimeException("All Field are mandatory");
        }
        user.setUser_id(UUID.randomUUID().toString());
        userProducer.senUser(user);
        return user;
    }

    public User getById(String id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("The User with" +id+ "Not Found"));
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Token getToken(User user) {
        Token token= restTemplate.postForObject("http://localhost:6062/api/v1/users/login", user, Token.class);
        return token;
    }
}
