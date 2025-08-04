package user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.model.Token;
import user_service.model.User;
import user_service.request.LoginRequest;
import user_service.service.JwtService;
import user_service.service.UserService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;
    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @GetMapping("/get")
    ResponseEntity<?> get(@RequestHeader("Authorization") String authHeader){
        if(authHeader!=null && authHeader.startsWith("Bearer")){
            String token= authHeader.substring(7);
            String userId= jwtService.extractUserId(token);
            return ResponseEntity.status(HttpStatus.OK).body(userService.getById(userId));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is Required");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        User user= userService.getByEmail(loginRequest.getEmail());
        if(Objects.equals(user.getPassword(), loginRequest.getPassword())){
            Token token= userService.getToken(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authorization", "Bearer " + token.getAccessToken())
                    .header("Authorization", "Bearer " + token.getRefreshToken())
                    .body(token);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is a problem !!!!!");
        }
    }
}
