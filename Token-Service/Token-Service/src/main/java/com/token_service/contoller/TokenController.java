package com.token_service.contoller;

import com.token_service.entities.Token;
import com.token_service.entities.User;
import com.token_service.response.JwtResponse;
import com.token_service.service.JwtTokenService;
import com.token_service.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TokenController {
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtTokenService jwtService;
    @PostMapping("/users/login")
    ResponseEntity<?> login(@RequestBody User user){
        try {
            if(refreshTokenService.findByToken(user.getUser_id())!=null){
                refreshTokenService.deleteToken(refreshTokenService.findByToken(user.getUser_id()));
            }
            Token refreshToken= refreshTokenService.create(user);
            String accessToken= jwtService.generateToken(user.getUser_id());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            JwtResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken.getToken())
                                    .build());
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/token/accessToken")
    ResponseEntity<?> accessToken(@RequestHeader("Authorization") String token){
        try {
            if(token!=null && token.startsWith("Bearer")){
                token= token.substring(7);
                Token refreshToken= refreshTokenService.findByToken(token);
                if(refreshTokenService.verifyRefreshToken(refreshToken)){
                    String accessToken= jwtService.generateToken(refreshToken.getUserId());
                    return ResponseEntity.ok()
                            .header("Authorization", "Bearer " + accessToken)
                            .header("Authorization", "Bearer " + refreshToken.getToken())
                            .body(
                                    JwtResponse.builder()
                                            .accessToken(accessToken)
                                            .refreshToken(refreshToken.getToken())
                                            .build()
                            );
                }
                else{
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token.");
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token.");
        }
    }
}
