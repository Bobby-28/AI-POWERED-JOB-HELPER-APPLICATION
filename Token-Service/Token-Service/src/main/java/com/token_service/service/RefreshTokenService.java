package com.token_service.service;

import com.token_service.entities.Token;
import com.token_service.entities.User;
import org.springframework.stereotype.Service;


public interface RefreshTokenService {
    Token create(User user);
    Boolean verifyRefreshToken(Token refreshToken);

    void deleteToken(Token refreshToken);
    Token findByToken(String token);
}
