package com.api_gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    Logger logger= LoggerFactory.getLogger(JwtService.class);
    private final String SECRET= "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    private Key getSignKey(){
        byte[] KeyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
    public String extractUserId(String token){
        return extractClaim(token).getSubject();
    }
    public Date extractExpiration(String token){
        return extractClaim(token).getExpiration();
    }
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Invalid access token: {}", e.getMessage());
            return false;
        }
    }
    private Claims extractClaim(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
