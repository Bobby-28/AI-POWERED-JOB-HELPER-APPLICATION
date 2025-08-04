package com.token_service.Impl;

import com.token_service.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    private static final String SECRET= "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    Logger logger= LoggerFactory.getLogger(JwtTokenServiceImpl.class);
    @Override
    public String extractUserId(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Invalid access token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String generateToken(String userId) {
        logger.info("Generating JWT token for user: {}", userId);
        Map<String, Object> claims= new HashMap<>();
        return createToken(claims, userId);
    }

    @Override
    public String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            logger.error("Error parsing token: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid token" + e);
        }
    }

    @Override
    public Key getSignKey() {
        byte[] KeyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
}
