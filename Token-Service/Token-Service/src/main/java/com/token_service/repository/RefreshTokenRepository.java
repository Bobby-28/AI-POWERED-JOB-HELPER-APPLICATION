package com.token_service.repository;

import com.token_service.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<Token, String> {
    Token findByToken(String token);
}
